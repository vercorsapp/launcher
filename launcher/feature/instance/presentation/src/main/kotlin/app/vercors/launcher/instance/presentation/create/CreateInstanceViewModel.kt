/*
 * Copyright (c) 2024-2025 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.launcher.instance.presentation.create

import app.vercors.launcher.core.presentation.mvi.MviViewModel
import app.vercors.launcher.game.domain.loader.LoaderVersion
import app.vercors.launcher.game.domain.loader.LoaderVersionRepository
import app.vercors.launcher.game.domain.loader.ModLoaderType
import app.vercors.launcher.game.domain.version.GameVersionList
import app.vercors.launcher.game.domain.version.GameVersionRepository
import app.vercors.launcher.instance.domain.Instance
import app.vercors.launcher.instance.domain.InstanceModLoader
import app.vercors.launcher.instance.domain.InstanceRepository
import app.vercors.lib.domain.Resource
import kotlinx.coroutines.Job
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CreateInstanceViewModel(
    private val gameVersionRepository: GameVersionRepository,
    private val loaderVersionRepository: LoaderVersionRepository,
    private val instanceRepository: InstanceRepository,
) : MviViewModel<CreateInstanceUiState, CreateInstanceUiIntent, CreateInstanceUiEffect>(CreateInstanceUiState()) {
    private var loaderVersionsJob: Job? = null

    override fun onStart() {
        super.onStart()
        resetState()
        collectInScope(gameVersionRepository.observeVersionList()) {
            when (it) {
                is Resource.Error -> {} // TODO
                Resource.Loading -> {} // TODO
                is Resource.Success -> onIntent(GameVersionsUpdated(it.value))
            }
        }
    }

    override fun afterStateUpdate(oldState: CreateInstanceUiState, newState: CreateInstanceUiState) {
        if (oldState.selectedGameVersionId != newState.selectedGameVersionId && newState.selectedGameVersionId != null) {
            loaderVersionsJob?.cancel()
            loaderVersionsJob =
                collectInScope(loaderVersionRepository.observeAllLoadersVersions(newState.selectedGameVersionId)) {
                when (it) {
                    is Resource.Error -> {} // TODO
                    Resource.Loading -> {} // TODO
                    is Resource.Success -> onIntent(LoaderVersionsUpdated(it.value))
                }
            }
        }
    }

    override fun ReductionState.reduce(intent: CreateInstanceUiIntent) = when (intent) {
        is CreateInstanceUiIntent.UpdateInstanceName -> update { copy(instanceName = intent.value) }
        is CreateInstanceUiIntent.SelectGameVersion -> update { copy(selectedGameVersionId = intent.value) }
        is CreateInstanceUiIntent.ToggleShowSnapshots -> update { copy(showSnapshots = intent.value) }
        is CreateInstanceUiIntent.SelectModLoader -> selectModLoader(intent.value)
        is CreateInstanceUiIntent.SelectModLoaderVersion -> update { copy(selectedModLoaderVersion = intent.value) }
        CreateInstanceUiIntent.CreateInstance -> createInstance()
        CreateInstanceUiIntent.CloseDialog -> effect(CreateInstanceUiEffect.CloseDialog)
        is GameVersionsUpdated -> update {
            copy(
            gameVersions = intent.list.toUi(),
            selectedGameVersionId = intent.list.latestReleaseId
            )
        }

        is LoaderVersionsUpdated -> update {
            copy(
            modLoaderVersions = intent.map.mapValues { (_, v) -> v.toUi() },
            modLoader = if (modLoader in intent.map) modLoader else null,
            selectedModLoaderVersion = intent.map[modLoader]?.toUi()?.first()
            )
        }
    }

    private fun ReductionState.selectModLoader(type: ModLoaderType?) {
        return update {
            if (type == null) copy(modLoader = null, selectedModLoaderVersion = null)
            else copy(modLoader = type, selectedModLoaderVersion = modLoaderVersions?.get(type)?.first())
        }
    }

    private fun ReductionState.createInstance() {
        runInScope {
            val stateValue = state.value
            instanceRepository.create(
                Instance(
                    name = stateValue.instanceName,
                    gameVersion = stateValue.selectedGameVersionId!!,
                    modLoader = if (stateValue.modLoader != null) InstanceModLoader(
                        type = stateValue.modLoader,
                        version = stateValue.selectedModLoaderVersion!!
                    ) else null
                )
            )
        }
        effect(CreateInstanceUiEffect.CloseDialog)
    }

    @JvmInline
    private value class GameVersionsUpdated(val list: GameVersionList) : CreateInstanceUiIntent

    @JvmInline
    private value class LoaderVersionsUpdated(val map: Map<ModLoaderType, List<LoaderVersion>>) : CreateInstanceUiIntent
}

/*
 * Copyright (c) 2024 skyecodes
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

import app.vercors.launcher.core.domain.Resource
import app.vercors.launcher.core.presentation.mvi.MviViewModel
import app.vercors.launcher.game.domain.loader.LoaderVersion
import app.vercors.launcher.game.domain.loader.LoaderVersionRepository
import app.vercors.launcher.game.domain.loader.ModLoaderType
import app.vercors.launcher.game.domain.version.GameVersionList
import app.vercors.launcher.game.domain.version.GameVersionRepository
import app.vercors.launcher.instance.domain.Instance
import app.vercors.launcher.instance.domain.InstanceModLoader
import app.vercors.launcher.instance.domain.InstanceRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CreateInstanceViewModel(
    private val gameVersionRepository: GameVersionRepository,
    private val loaderVersionRepository: LoaderVersionRepository,
    private val instanceRepository: InstanceRepository,
) : MviViewModel<CreateInstanceUiState, CreateInstanceUiIntent, CreateInstanceUiEffect>(CreateInstanceUiState()) {
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

    override fun onStateUpdate(state: CreateInstanceUiState) {
        super.onStateUpdate(state)
        if (state.selectedGameVersionId != null && state.modLoader != null) {
            collectInScope(
                loaderVersionRepository.observeLoaderVersions(
                    loader = state.modLoader,
                    gameVersion = state.selectedGameVersionId
                )
            ) {
                when (it) {
                    is Resource.Error -> {} // TODO
                    Resource.Loading -> {} // TODO
                    is Resource.Success -> onIntent(LoaderVersionsUpdated(it.value))
                }
            }
        }
    }

    override fun CreateInstanceUiState.reduce(intent: CreateInstanceUiIntent): CreateInstanceUiState = when (intent) {
        is CreateInstanceUiIntent.UpdateInstanceName -> copy(instanceName = intent.value)
        is CreateInstanceUiIntent.SelectGameVersion -> copy(selectedGameVersionId = intent.value)
        is CreateInstanceUiIntent.ToggleShowSnapshots -> copy(showSnapshots = intent.value)
        is CreateInstanceUiIntent.SelectModLoader -> selectModLoader(intent.value)
        is CreateInstanceUiIntent.SelectModLoaderVersion -> copy(selectedModLoaderVersion = intent.value)
        CreateInstanceUiIntent.CreateInstance -> createInstance().withEffect(CreateInstanceUiEffect.CloseDialog)
        CreateInstanceUiIntent.CloseDialog -> withEffect(CreateInstanceUiEffect.CloseDialog)
        is GameVersionsUpdated -> copy(
            gameVersions = intent.list.toUi(),
            selectedGameVersionId = intent.list.latestReleaseId
        )

        is LoaderVersionsUpdated -> copy(
            modLoaderVersions = intent.list?.toUi(),
            selectedModLoaderVersion = intent.list?.firstOrNull()?.id
        )
    }

    private fun CreateInstanceUiState.selectModLoader(type: ModLoaderType?): CreateInstanceUiState {
        return if (type == null) copy(modLoader = null, modLoaderVersions = null, selectedModLoaderVersion = null)
        else copy(modLoader = type)
    }

    private fun CreateInstanceUiState.createInstance(): CreateInstanceUiState = apply {
        runInScope {
            instanceRepository.create(
                Instance(
                    name = instanceName,
                    gameVersion = selectedGameVersionId!!,
                    modLoader = if (modLoader != null) InstanceModLoader(
                        type = modLoader,
                        version = selectedModLoaderVersion!!
                    ) else null
                )
            )
        }
    }

    @JvmInline
    private value class GameVersionsUpdated(val list: GameVersionList) : CreateInstanceUiIntent

    @JvmInline
    private value class LoaderVersionsUpdated(val list: List<LoaderVersion>?) : CreateInstanceUiIntent
}

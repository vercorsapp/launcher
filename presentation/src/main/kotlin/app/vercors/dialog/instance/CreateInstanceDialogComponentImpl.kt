/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.dialog.instance

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.ModLoader
import app.vercors.common.inject
import app.vercors.instance.InstanceData
import app.vercors.instance.InstanceRepository
import app.vercors.instance.mojang.MojangRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CreateInstanceDialogComponentImpl(
    componentContext: AppComponentContext,
    private val _onClose: () -> Unit,
    private val mojangRepository: MojangRepository = componentContext.inject(),
    private val instanceRepository: InstanceRepository = componentContext.inject()
) : AbstractAppComponent(componentContext, KotlinLogging.logger {}), CreateInstanceDialogComponent {
    private val _state = MutableStateFlow(CreateInstanceDialogState())
    override val state: StateFlow<CreateInstanceDialogState> = _state

    init {
        launchInLifecycle { loadMinecraftVersions() }
    }

    private suspend fun loadMinecraftVersions() {
        val manifest = mojangRepository.getVersionManifest()
        val data = manifest.versions.map {
            CreateInstanceDialogMinecraftVersion(
                data = it,
                isLatestRelease = manifest.isLatestRelease(it),
                isLatestSnapshot = manifest.isLatestSnapshot(it)
            )
        }
        _state.update {
            it.copy(allMinecraftVersions = data).let { state ->
                state.copy(minecraftVersion = state.filteredMinecraftVersions.firstOrNull())
            }
        }
    }

    override fun onIntent(intent: CreateInstanceDialogIntent) = when (intent) {
        is CreateInstanceDialogIntent.UpdateInstanceName -> onUpdateInstanceName(intent.instanceName)
        is CreateInstanceDialogIntent.UpdateMinecraftVersion -> onUpdateMinecraftVersion(intent.minecraftVersion)
        is CreateInstanceDialogIntent.UpdateIncludeSnapshots -> onUpdateIncludeSnapshots(intent.includeSnapshots)
        CreateInstanceDialogIntent.ToggleIncludeSnapshots -> onToggleIncludeSnapshots()
        is CreateInstanceDialogIntent.UpdateLoader -> onUpdateLoader(intent.loader)
        CreateInstanceDialogIntent.CreateInstance -> onCreateInstance()
        CreateInstanceDialogIntent.CloseDialog -> onClose()
    }

    private fun onUpdateInstanceName(instanceName: String) {
        _state.update { it.copy(instanceName = instanceName) }
    }

    private fun onUpdateMinecraftVersion(minecraftVersion: CreateInstanceDialogMinecraftVersion) {
        _state.update { it.copy(minecraftVersion = minecraftVersion) }
    }

    private fun onUpdateIncludeSnapshots(includeSnapshots: Boolean) {
        _state.update {
            it.copy(includeSnapshots = includeSnapshots).let { state ->
                state.copy(
                    minecraftVersion = if (state.minecraftVersion in state.filteredMinecraftVersions) state.minecraftVersion
                    else state.filteredMinecraftVersions.firstOrNull()
                )
            }
        }
    }

    private fun onToggleIncludeSnapshots() {
        onUpdateIncludeSnapshots(!state.value.includeSnapshots)
    }

    private fun onUpdateLoader(loader: ModLoader?) {
        _state.update { it.copy(loader = loader) }
    }

    private fun onCreateInstance() {
        val state = state.value
        if (state.isValid) {
            localScope.launch {
                instanceRepository.createInstance(
                    InstanceData(
                        name = state.instanceName,
                        gameVersion = state.minecraftVersion!!.data,
                        loader = state.loader,
                        loaderVersion = state.loaderVersion
                    )
                )
                onClose()
            }
        }
    }

    override fun onClose() = _onClose()
}
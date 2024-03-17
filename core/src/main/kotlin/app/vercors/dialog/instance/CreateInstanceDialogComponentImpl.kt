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
import app.vercors.common.inject
import app.vercors.instance.InstanceData
import app.vercors.instance.InstanceService
import app.vercors.instance.mojang.MojangService
import app.vercors.project.ModLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CreateInstanceDialogComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val mojangService: MojangService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject()
) : AbstractAppComponent(componentContext), CreateInstanceDialogComponent {
    private val _uiState = MutableStateFlow(CreateInstanceDialogUiState())
    override val uiState: StateFlow<CreateInstanceDialogUiState> = _uiState

    init {
        launchInLifecycle { loadMinecraftVersions() }
    }

    private suspend fun loadMinecraftVersions() {
        val manifest = mojangService.getVersionManifest()
        val data = manifest.versions.map {
            CreateInstanceDialogMinecraftVersion(
                data = it,
                isLatestRelease = manifest.isLatestRelease(it),
                isLatestSnapshot = manifest.isLatestSnapshot(it)
            )
        }
        _uiState.update {
            it.copy(allMinecraftVersions = data).let { state ->
                state.copy(minecraftVersion = state.filteredMinecraftVersions.firstOrNull())
            }
        }
    }

    override fun updateInstanceName(instanceName: String) {
        _uiState.update { it.copy(instanceName = instanceName) }
    }

    override fun updateMinecraftVersion(minecraftVersion: CreateInstanceDialogMinecraftVersion) {
        _uiState.update { it.copy(minecraftVersion = minecraftVersion) }
    }

    override fun updateIncludeSnapshots(includeSnapshots: Boolean) {
        _uiState.update {
            it.copy(includeSnapshots = includeSnapshots).let { state ->
                state.copy(
                    minecraftVersion = if (state.minecraftVersion in state.filteredMinecraftVersions) state.minecraftVersion
                    else state.filteredMinecraftVersions.firstOrNull()
                )
            }
        }
    }

    override fun toggleIncludeSnapshots() {
        updateIncludeSnapshots(!uiState.value.includeSnapshots)
    }

    override fun updateLoader(loader: ModLoader?) {
        _uiState.update { it.copy(loader = loader) }
    }

    override fun createInstance() {
        val state = uiState.value
        if (state.isValid) {
            instanceService.createInstance(
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
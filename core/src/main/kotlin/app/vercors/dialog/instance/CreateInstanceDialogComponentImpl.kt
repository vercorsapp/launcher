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
import kotlinx.coroutines.launch

class CreateInstanceDialogComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val mojangService: MojangService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject()
) : AbstractAppComponent(componentContext), CreateInstanceDialogComponent {
    private val _uiState = MutableStateFlow(CreateInstanceDialogUiState())
    override val uiState: StateFlow<CreateInstanceDialogUiState> = _uiState

    override fun onCreate() {
        super.onCreate()
        launch { loadMinecraftVersions() }
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
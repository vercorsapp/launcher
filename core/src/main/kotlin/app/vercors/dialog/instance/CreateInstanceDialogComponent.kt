package app.vercors.dialog.instance

import app.vercors.dialog.DialogChildComponent
import app.vercors.project.ModLoader
import kotlinx.coroutines.flow.StateFlow

interface CreateInstanceDialogComponent : DialogChildComponent {
    val uiState: StateFlow<CreateInstanceDialogUiState>

    fun updateInstanceName(instanceName: String)
    fun updateMinecraftVersion(minecraftVersion: CreateInstanceDialogMinecraftVersion)
    fun updateIncludeSnapshots(includeSnapshots: Boolean)
    fun toggleIncludeSnapshots()
    fun updateLoader(loader: ModLoader?)
    fun createInstance()
}
package app.vercors.dialog.instance

import app.vercors.instance.mojang.data.MojangReleaseType
import app.vercors.project.ModLoader

data class CreateInstanceDialogUiState(
    val instanceName: String = "",
    val minecraftVersion: CreateInstanceDialogMinecraftVersion? = null,
    val includeSnapshots: Boolean = false,
    val allMinecraftVersions: List<CreateInstanceDialogMinecraftVersion> = listOf(),
    val loader: ModLoader? = null,
    val loaderVersion: String? = null,
) {
    val isValid: Boolean = instanceName.filter { it.isLetterOrDigit() }.isNotBlank()
            && minecraftVersion != null
            && (loader == null || !loaderVersion.isNullOrBlank())
    val filteredMinecraftVersions: List<CreateInstanceDialogMinecraftVersion> = allMinecraftVersions
        .filter { it.data.type === MojangReleaseType.Release || includeSnapshots }
}

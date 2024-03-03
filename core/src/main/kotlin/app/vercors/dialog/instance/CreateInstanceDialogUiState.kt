package app.vercors.dialog.instance

import app.vercors.instance.mojang.data.MojangReleaseType
import app.vercors.project.ModLoader
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class CreateInstanceDialogUiState(
    val instanceName: String = "",
    val minecraftVersion: CreateInstanceDialogMinecraftVersion? = null,
    val includeSnapshots: Boolean = false,
    val allMinecraftVersions: ImmutableList<CreateInstanceDialogMinecraftVersion> = persistentListOf(),
    val loader: ModLoader? = null,
    val loaderVersion: String? = null,
) {
    val isValid: Boolean = instanceName.filter { it.isLetterOrDigit() }.isNotBlank()
            && minecraftVersion != null
            && (loader == null || !loaderVersion.isNullOrBlank())
    val filteredMinecraftVersions: ImmutableList<CreateInstanceDialogMinecraftVersion> = allMinecraftVersions
        .filter { it.data.type === MojangReleaseType.Release || includeSnapshots }
        .toImmutableList()
}

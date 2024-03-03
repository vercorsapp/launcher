package app.vercors.dialog.instance

import app.vercors.instance.mojang.data.MojangVersionManifest

data class CreateInstanceDialogMinecraftVersion(
    val data: MojangVersionManifest.Version,
    val isLatestRelease: Boolean,
    val isLatestSnapshot: Boolean
)
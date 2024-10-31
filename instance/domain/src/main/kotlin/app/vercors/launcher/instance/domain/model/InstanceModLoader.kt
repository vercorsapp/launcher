package app.vercors.launcher.instance.domain.model

import app.vercors.launcher.game.domain.ModLoaderType

data class InstanceModLoader(
    val type: ModLoaderType,
    val version: String
)

package app.vercors.launcher.instance.domain

import app.vercors.launcher.game.domain.loader.ModLoaderType

data class InstanceModLoader(
    val type: ModLoaderType,
    val version: String
)
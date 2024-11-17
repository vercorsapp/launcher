package app.vercors.launcher.game.data

import app.vercors.launcher.game.domain.loader.ModLoaderType

fun ModLoaderType.toStringData(): String = when (this) {
    ModLoaderType.Forge -> "forge"
    ModLoaderType.Fabric -> "fabric"
    ModLoaderType.Neoforge -> "neoforge"
    ModLoaderType.Quilt -> "quilt"
}

fun String.toModLoaderType(): ModLoaderType = when (this) {
    "forge" -> ModLoaderType.Forge
    "fabric" -> ModLoaderType.Fabric
    "neoforge" -> ModLoaderType.Neoforge
    "quilt" -> ModLoaderType.Quilt
    else -> throw IllegalArgumentException("Invalid ModLoaderType: $this")
}
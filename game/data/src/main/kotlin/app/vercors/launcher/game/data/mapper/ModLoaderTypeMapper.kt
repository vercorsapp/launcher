package app.vercors.launcher.game.data.mapper

import app.vercors.launcher.game.domain.ModLoaderType
import org.koin.core.annotation.Single

@Single
class ModLoaderTypeMapper {
    fun fromData(data: String): ModLoaderType = when (data) {
        "forge" -> ModLoaderType.Forge
        "fabric" -> ModLoaderType.Fabric
        "neoforge" -> ModLoaderType.Neoforge
        "quilt" -> ModLoaderType.Quilt
        else -> throw IllegalArgumentException("Invalid ModLoaderType: $data")
    }

    fun toData(type: ModLoaderType): String = when (type) {
        ModLoaderType.Forge -> "forge"
        ModLoaderType.Fabric -> "fabric"
        ModLoaderType.Neoforge -> "neoforge"
        ModLoaderType.Quilt -> "quilt"
    }
}
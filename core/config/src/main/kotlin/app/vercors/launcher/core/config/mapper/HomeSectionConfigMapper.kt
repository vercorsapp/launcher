package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.proto.HomeSectionProto

fun HomeSectionProto.toConfig(): HomeSectionConfig = when (this) {
    HomeSectionProto.JUMP_BACK_IN -> HomeSectionConfig.JumpBackIn
    HomeSectionProto.POPULAR_MODS -> HomeSectionConfig.PopularMods
    HomeSectionProto.POPULAR_MODPACKS -> HomeSectionConfig.PopularModpacks
    HomeSectionProto.POPULAR_RESOURCE_PACKS -> HomeSectionConfig.PopularResourcePacks
    HomeSectionProto.POPULAR_SHADER_PACKS -> HomeSectionConfig.PopularShaderPacks
    HomeSectionProto.UNRECOGNIZED -> HomeSectionConfig.entries.first()
}

fun HomeSectionConfig.toProto(): HomeSectionProto = when (this) {
    HomeSectionConfig.JumpBackIn -> HomeSectionProto.JUMP_BACK_IN
    HomeSectionConfig.PopularMods -> HomeSectionProto.POPULAR_MODS
    HomeSectionConfig.PopularModpacks -> HomeSectionProto.POPULAR_MODPACKS
    HomeSectionConfig.PopularResourcePacks -> HomeSectionProto.POPULAR_RESOURCE_PACKS
    HomeSectionConfig.PopularShaderPacks -> HomeSectionProto.POPULAR_SHADER_PACKS
}

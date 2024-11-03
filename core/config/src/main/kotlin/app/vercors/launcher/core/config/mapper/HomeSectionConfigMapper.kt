package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.proto.HomeSection
import org.koin.core.annotation.Single

@Single
class HomeSectionConfigMapper {
    fun fromProto(homeSection: HomeSection): HomeSectionConfig = when (homeSection) {
        HomeSection.JUMP_BACK_IN -> HomeSectionConfig.JumpBackIn
        HomeSection.POPULAR_MODS -> HomeSectionConfig.PopularMods
        HomeSection.POPULAR_MODPACKS -> HomeSectionConfig.PopularModpacks
        HomeSection.POPULAR_RESOURCE_PACKS -> HomeSectionConfig.PopularResourcePacks
        HomeSection.POPULAR_SHADER_PACKS -> HomeSectionConfig.PopularShaderPacks
        HomeSection.UNRECOGNIZED -> HomeSectionConfig.entries.first()
    }

    fun toProto(homeSectionConfig: HomeSectionConfig): HomeSection = when (homeSectionConfig) {
        HomeSectionConfig.JumpBackIn -> HomeSection.JUMP_BACK_IN
        HomeSectionConfig.PopularMods -> HomeSection.POPULAR_MODS
        HomeSectionConfig.PopularModpacks -> HomeSection.POPULAR_MODPACKS
        HomeSectionConfig.PopularResourcePacks -> HomeSection.POPULAR_RESOURCE_PACKS
        HomeSectionConfig.PopularShaderPacks -> HomeSection.POPULAR_SHADER_PACKS
    }
}

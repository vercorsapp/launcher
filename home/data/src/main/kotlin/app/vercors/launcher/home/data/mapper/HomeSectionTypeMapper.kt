package app.vercors.launcher.home.data.mapper

import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.home.domain.model.HomeSectionType

fun HomeSectionConfig.toType(): HomeSectionType = when (this) {
    HomeSectionConfig.JumpBackIn -> HomeSectionType.JumpBackIn
    HomeSectionConfig.PopularMods -> HomeSectionType.PopularMods
    HomeSectionConfig.PopularModpacks -> HomeSectionType.PopularModpacks
    HomeSectionConfig.PopularResourcePacks -> HomeSectionType.PopularResourcePacks
    HomeSectionConfig.PopularShaderPacks -> HomeSectionType.PopularShaderPacks
}
package app.vercors.launcher.home.data

import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.home.domain.HomeSectionType

fun HomeSectionConfig.toType(): HomeSectionType = when (this) {
    HomeSectionConfig.JumpBackIn -> HomeSectionType.JumpBackIn
    HomeSectionConfig.PopularMods -> HomeSectionType.PopularMods
    HomeSectionConfig.PopularModpacks -> HomeSectionType.PopularModpacks
    HomeSectionConfig.PopularResourcePacks -> HomeSectionType.PopularResourcePacks
    HomeSectionConfig.PopularShaderPacks -> HomeSectionType.PopularShaderPacks
}
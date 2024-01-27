package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.ui.Localization
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HomeSectionType(
    val localizedTitle: (Localization) -> String,
    val localizedShortTitle: (Localization) -> String
) {
    @SerialName("jumpBackIn")
    JumpBackIn(Localization::jumpBackIn, Localization::instances),

    @SerialName("popularMods")
    PopularMods(Localization::popularMods, Localization::mods),

    @SerialName("popularModpacks")
    PopularModpacks(Localization::popularModpacks, Localization::modpacks),

    @SerialName("popularResourcePacks")
    PopularResourcePacks(Localization::popularResourcePacks, Localization::resourcePacks),

    @SerialName("popularShaderPacks")
    PopularShaderPacks(Localization::popularShaderPacks, Localization::shaderPacks)
}
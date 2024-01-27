package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.ui.Localization
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HomeSectionType(val localizedTitle: (Localization) -> String) {
    @SerialName("jumpBackIn")
    JumpBackIn(Localization::jumpBackIn),

    @SerialName("popularMods")
    PopularMods(Localization::popularMods),

    @SerialName("popularModpacks")
    PopularModpacks(Localization::popularModpacks),

    @SerialName("popularResourcePacks")
    PopularResourcePacks(Localization::popularResourcePacks),

    @SerialName("popularShaderPacks")
    PopularShaderPacks(Localization::popularShaderPacks)
}
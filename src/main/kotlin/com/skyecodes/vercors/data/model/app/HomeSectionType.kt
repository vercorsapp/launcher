package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.ui.UI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HomeSectionType(val localizedTitle: (UI.Localization) -> String) {
    @SerialName("jumpBackIn")
    JumpBackIn(UI.Localization::jumpBackIn),

    @SerialName("popularMods")
    PopularMods(UI.Localization::popularMods),

    @SerialName("popularModpacks")
    PopularModpacks(UI.Localization::popularModpacks),

    @SerialName("popularResourcePacks")
    PopularResourcePacks(UI.Localization::popularResourcePacks),

    @SerialName("popularShaderPacks")
    PopularShaderPacks(UI.Localization::popularShaderPacks)
}
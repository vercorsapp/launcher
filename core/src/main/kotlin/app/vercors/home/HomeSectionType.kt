package app.vercors.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HomeSectionType {
    @SerialName("jumpBackIn")
    JumpBackIn,

    @SerialName("popularMods")
    PopularMods,

    @SerialName("popularModpacks")
    PopularModpacks,

    @SerialName("popularResourcePacks")
    PopularResourcePacks,

    @SerialName("popularShaderPacks")
    PopularShaderPacks
}
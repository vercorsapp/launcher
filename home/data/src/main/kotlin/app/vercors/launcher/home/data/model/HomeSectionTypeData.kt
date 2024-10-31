package app.vercors.launcher.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HomeSectionTypeData {
    @SerialName("jumpBackIn")
    JumpBackIn,
    @SerialName("popularModpacks")
    PopularModpacks,
    @SerialName("popularMods")
    PopularMods,
    @SerialName("popularResourcePacks")
    PopularResourcePacks,
    @SerialName("popularShaderPacks")
    PopularShaderPacks
}

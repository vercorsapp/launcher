package app.vercors.launcher.home.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeConfigData(
    val sections: List<HomeSectionTypeData>,
    val provider: String
)

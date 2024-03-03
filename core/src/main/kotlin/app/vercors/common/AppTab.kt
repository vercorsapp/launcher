package app.vercors.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppTab {
    @SerialName("home")
    Home,

    @SerialName("instances")
    Instances,

    @SerialName("search")
    Search,

    @SerialName("settings")
    Settings
}
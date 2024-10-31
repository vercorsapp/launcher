package app.vercors.launcher.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ThemeData {
    @SerialName("light")
    Light,
    @SerialName("system")
    System,
    @SerialName("dark")
    Dark
}

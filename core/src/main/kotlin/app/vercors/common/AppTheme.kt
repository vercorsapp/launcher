package app.vercors.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppTheme {
    @SerialName("light")
    Light,

    @SerialName("system")
    System,

    @SerialName("dark")
    Dark
}
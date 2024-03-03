package app.vercors.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppDarkTheme {
    @SerialName("lighter")
    Lighter,

    @SerialName("normal")
    Normal,

    @SerialName("darker")
    Darker
}
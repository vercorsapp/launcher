package com.skyecodes.vercors.data.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppTheme {
    @SerialName("system")
    SYSTEM,

    @SerialName("light")
    LIGHT,

    @SerialName("dark")
    DARK
}
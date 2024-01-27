package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.ui.Localization
import compose.icons.FeatherIcons
import compose.icons.feathericons.Cpu
import compose.icons.feathericons.Moon
import compose.icons.feathericons.Sun
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppTheme(val localizedText: (Localization) -> String, val icon: ImageVector) {
    @SerialName("system")
    System(Localization::system, FeatherIcons.Cpu),

    @SerialName("light")
    Light(Localization::light, FeatherIcons.Sun),

    @SerialName("dark")
    Dark(Localization::dark, FeatherIcons.Moon)
}
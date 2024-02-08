package com.skyecodes.vercors.menu

import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.common.Localization
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Home
import compose.icons.feathericons.Search
import compose.icons.feathericons.Settings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppTab(
    val localizedTitle: (Localization) -> String,
    val icon: ImageVector,
) {
    @SerialName("home")
    Home(Localization::home, FeatherIcons.Home),

    @SerialName("instances")
    Instances(Localization::instances, FeatherIcons.Box),

    @SerialName("search")
    Search(Localization::search, FeatherIcons.Search),

    @SerialName("settings")
    Settings(Localization::settings, FeatherIcons.Settings)
}
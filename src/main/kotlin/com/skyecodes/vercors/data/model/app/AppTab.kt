package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.ui.UI
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppTab(
    val localizedTitle: (UI.Localization) -> String,
    val icon: ImageVector,
) {
    @SerialName("home")
    Home(UI.Localization::home, FeatherIcons.Home),

    @SerialName("instances")
    Instances(UI.Localization::instances, FeatherIcons.Box),

    @SerialName("search")
    Search(UI.Localization::search, FeatherIcons.Search),

    @SerialName("accounts")
    Accounts(UI.Localization::accounts, FeatherIcons.Users),

    @SerialName("settings")
    Settings(UI.Localization::settings, FeatherIcons.Settings)
}
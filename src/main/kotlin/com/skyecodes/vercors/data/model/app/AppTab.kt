package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.ui.Localization
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
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

    @SerialName("accounts")
    Accounts(Localization::accounts, FeatherIcons.Users),

    @SerialName("settings")
    Settings(Localization::settings, FeatherIcons.Settings)
}
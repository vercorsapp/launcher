package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import com.skyecodes.vercors.ui.UI
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import kotlinx.serialization.Serializable

@Serializable(AppTabSerializer::class)
enum class AppTab(
    val title: String,
    val icon: ImageVector,
    override val value: String
) : StringEnumerable {
    Home(UI.Text.HOME, FeatherIcons.Home, "home"),
    Instances(UI.Text.INSTANCES, FeatherIcons.Box, "instances"),
    Search(UI.Text.SEARCH, FeatherIcons.Search, "search"),
    Accounts(UI.Text.ACCOUNTS, FeatherIcons.Users, "accounts"),
    Settings(UI.Text.SETTINGS, FeatherIcons.Settings, "settings");
}

private class AppTabSerializer : StringEnumerableSerializer<AppTab>(AppTab.entries)
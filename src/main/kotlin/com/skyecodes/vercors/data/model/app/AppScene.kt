package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import com.skyecodes.vercors.ui.UI
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import kotlinx.serialization.Serializable

@Serializable(AppSceneSerializer::class)
enum class AppScene(
    val title: String,
    val icon: ImageVector,
    override val value: String,
    val route: String
) : StringEnumerable {
    Home(UI.Text.HOME, FeatherIcons.Home, "home", "/home"),
    Instances(UI.Text.INSTANCES, FeatherIcons.Box, "instances", "/instances"),
    Search(UI.Text.SEARCH, FeatherIcons.Search, "search", "/search"),
    Accounts(UI.Text.ACCOUNTS, FeatherIcons.Users, "accounts", "/accounts"),
    Settings(UI.Text.SETTINGS, FeatherIcons.Settings, "settings", "/settings");
}

private class AppSceneSerializer : StringEnumerableSerializer<AppScene>(AppScene.entries)
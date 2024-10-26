package app.vercors.launcher.app.presentation.util

import androidx.navigation.NavDestination
import app.vercors.launcher.app.presentation.state.NavigationTab
import app.vercors.launcher.core.generated.resources.*
import org.jetbrains.compose.resources.StringResource

typealias ScreenType = String

val NavDestination?.screenType: ScreenType?
    get() = this?.route?.substringAfterLast(".")?.substringBefore("?")

val ScreenType?.screenName: StringResource?
    get() = when (this) {
        "Home" -> Res.string.home
        "InstanceList" -> Res.string.instances
        "ProjectList" -> Res.string.projects
        "Accounts" -> Res.string.accounts
        "Settings" -> Res.string.settings
        else -> null
    }

val ScreenType?.currentTab: NavigationTab?
    get() = when (this) {
        "Home" -> NavigationTab.Home
        "InstanceList" -> NavigationTab.Instances
        "ProjectList" -> NavigationTab.Projects
        "Accounts" -> NavigationTab.Accounts
        "Settings" -> NavigationTab.Settings
        else -> null
    }

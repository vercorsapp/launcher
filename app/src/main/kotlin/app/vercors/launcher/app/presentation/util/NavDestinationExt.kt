package app.vercors.launcher.app.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import app.vercors.launcher.app.presentation.state.NavigationTab
import app.vercors.launcher.generated.resources.*
import org.jetbrains.compose.resources.stringResource

val NavDestination?.screenName: String
    @Composable get() = when (screenType) {
        "Home" -> stringResource(Res.string.home)
        "InstanceList" -> stringResource(Res.string.instances)
        "ProjectList" -> stringResource(Res.string.projects)
        "Accounts" -> stringResource(Res.string.accounts)
        "Settings" -> stringResource(Res.string.settings)
        else -> stringResource(Res.string.app_title)
    }


val NavDestination?.currentTab: NavigationTab?
    get() = when (screenType) {
        "Home" -> NavigationTab.Home
        "InstanceList" -> NavigationTab.Instances
        "ProjectList" -> NavigationTab.Projects
        "Accounts" -> NavigationTab.Accounts
        "Settings" -> NavigationTab.Settings
        else -> null
    }

private val NavDestination?.screenType: String? get() = this?.route?.substringAfterLast(".")?.substringBefore("?")
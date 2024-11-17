package app.vercors.launcher.app.navigation

import app.vercors.launcher.account.presentation.navigation.AccountBaseRoute
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.home.presentation.navigation.HomeBaseRoute
import app.vercors.launcher.instance.presentation.navigation.InstanceBaseRoute
import app.vercors.launcher.project.presentation.navigation.ProjectBaseRoute
import app.vercors.launcher.settings.presentation.navigation.SettingsBaseRoute
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class TopLevelDestination(val route: Any, val icon: DrawableResource, val title: StringResource) {
    Home(HomeBaseRoute, appDrawable { house }, appString { home }),
    Instances(InstanceBaseRoute, appDrawable { hard_drive }, appString { instances }),
    Projects(ProjectBaseRoute, appDrawable { folder_cog }, appString { projects }),
    Accounts(AccountBaseRoute, appDrawable { user }, appString { accounts }),
    Settings(SettingsBaseRoute, appDrawable { settings }, appString { settings }),
}
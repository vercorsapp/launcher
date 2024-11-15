package app.vercors.launcher.app.navigation

import app.vercors.launcher.account.presentation.navigation.AccountBaseRoute
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.home.presentation.navigation.HomeBaseRoute
import app.vercors.launcher.instance.presentation.navigation.InstanceBaseRoute
import app.vercors.launcher.project.presentation.navigation.ProjectBaseRoute
import app.vercors.launcher.settings.presentation.navigation.SettingsBaseRoute
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class TopLevelDestination(val route: Any, val icon: DrawableResource, val title: StringResource) {
    Home(HomeBaseRoute, CoreDrawable.house, CoreString.home),
    Instances(InstanceBaseRoute, CoreDrawable.hard_drive, CoreString.instances),
    Projects(ProjectBaseRoute, CoreDrawable.folder_cog, CoreString.projects),
    Accounts(AccountBaseRoute, CoreDrawable.user, CoreString.accounts),
    Settings(SettingsBaseRoute, CoreDrawable.settings, CoreString.settings),
}
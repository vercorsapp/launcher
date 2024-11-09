package app.vercors.launcher.app.navigation

import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class TopLevelDestination(val route: AppDestination, val icon: DrawableResource, val title: StringResource) {
    Home(AppDestination.Home, CoreDrawable.house, CoreString.home),
    Instances(AppDestination.InstanceList, CoreDrawable.hard_drive, CoreString.instances),
    Projects(AppDestination.ProjectList, CoreDrawable.folder_cog, CoreString.projects),
    Accounts(AppDestination.Accounts, CoreDrawable.user, CoreString.accounts),
    Settings(AppDestination.Settings, CoreDrawable.settings, CoreString.settings),
}
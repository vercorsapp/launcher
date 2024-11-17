package app.vercors.launcher.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.vercors.launcher.settings.presentation.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object SettingsRoute

@Serializable
data object SettingsBaseRoute

fun NavController.navigateToSettings(navOptions: NavOptions) = navigate(
    route = SettingsRoute,
    navOptions = navOptions
)

fun NavGraphBuilder.settingsSection() {
    navigation<SettingsBaseRoute>(startDestination = SettingsRoute) {
        composable<SettingsRoute> {
            SettingsScreen()
        }
    }
}
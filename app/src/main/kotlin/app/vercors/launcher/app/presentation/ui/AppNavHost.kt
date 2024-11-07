package app.vercors.launcher.app.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.vercors.launcher.app.navigation.AppDestination
import app.vercors.launcher.app.navigation.destination.HomeDestination
import app.vercors.launcher.app.navigation.destination.SettingsDestination
import app.vercors.launcher.core.presentation.ui.defaultEnterAnimation
import app.vercors.launcher.core.presentation.ui.defaultExitAnimation

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: AppDestination
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { defaultEnterAnimation },
        exitTransition = { defaultExitAnimation }
    ) {
        composable<AppDestination.Home> {
            HomeDestination()
        }
        composable<AppDestination.InstanceList> { }
        composable<AppDestination.ProjectList> { }
        composable<AppDestination.Accounts> { }
        composable<AppDestination.Settings> {
            SettingsDestination()
        }
    }
}
package app.vercors.launcher.app.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.vercors.launcher.app.navigation.AppDestination
import app.vercors.launcher.app.navigation.destination.HomeDestination
import app.vercors.launcher.app.navigation.destination.InstanceListDestination
import app.vercors.launcher.app.navigation.destination.SettingsDestination
import app.vercors.launcher.core.presentation.ui.AppAnimations
import app.vercors.launcher.core.presentation.ui.defaultEnterAnimation
import app.vercors.launcher.core.presentation.ui.defaultExitAnimation

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: AppDestination
) {
    val animations = AppAnimations.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { if (animations) defaultEnterAnimation else EnterTransition.None },
        exitTransition = { if (animations) defaultExitAnimation else ExitTransition.None },
    ) {
        composable<AppDestination.Home> {
            HomeDestination {
                // TODO handle navigation events
            }
        }
        composable<AppDestination.InstanceList> {
            InstanceListDestination()
        }
        composable<AppDestination.ProjectList> {

        }
        composable<AppDestination.Accounts> {

        }
        composable<AppDestination.Settings> {
            SettingsDestination()
        }
    }
}
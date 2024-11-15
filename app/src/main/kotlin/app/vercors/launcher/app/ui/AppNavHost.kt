package app.vercors.launcher.app.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import app.vercors.launcher.account.presentation.navigation.accountSection
import app.vercors.launcher.app.viewmodel.AppDialog
import app.vercors.launcher.app.viewmodel.AppUiIntent
import app.vercors.launcher.core.presentation.ui.AppAnimations
import app.vercors.launcher.core.presentation.ui.defaultEnterAnimation
import app.vercors.launcher.core.presentation.ui.defaultExitAnimation
import app.vercors.launcher.home.presentation.navigation.homeSection
import app.vercors.launcher.instance.presentation.navigation.instanceSection
import app.vercors.launcher.project.presentation.navigation.projectSection
import app.vercors.launcher.settings.presentation.navigation.settingsSection

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any,
    onIntent: (AppUiIntent) -> Unit
) {
    val animations = AppAnimations.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { if (animations) defaultEnterAnimation else EnterTransition.None },
        exitTransition = { if (animations) defaultExitAnimation else ExitTransition.None },
    ) {
        homeSection(
            onProjectClick = {},
            onProjectAction = {},
            onInstanceClick = {},
            onInstanceAction = {},
            onCreateInstanceClick = { onIntent(AppUiIntent.OpenDialog(AppDialog.CreateInstance)) }
        )
        instanceSection()
        projectSection()
        accountSection()
        settingsSection()
    }
}
package app.vercors.launcher.app.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.vercors.launcher.core.presentation.navigation.AppDestination
import app.vercors.launcher.core.presentation.navigation.NavigationAction
import app.vercors.launcher.core.presentation.navigation.Navigator
import app.vercors.launcher.core.presentation.ui.ObserveAsEvents
import app.vercors.launcher.core.presentation.ui.defaultEnterAnimation
import app.vercors.launcher.core.presentation.ui.defaultExitAnimation
import app.vercors.launcher.home.presentation.ui.HomeScreen
import app.vercors.launcher.home.presentation.viewmodel.HomeViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainContent(
    navController: NavHostController
) {
    val navigator = koinInject<Navigator>()
    ObserveAsEvents(navigator.navigationActions) {
        when (it) {
            NavigationAction.NavigateBack -> navController.navigateUp()
            is NavigationAction.NavigateTo -> navController.navigate(it.destination)
        }
    }

    NavHost(
        navController = navController,
        startDestination = navigator.startDestination,
        enterTransition = { defaultEnterAnimation },
        exitTransition = { defaultExitAnimation }
    ) {
        composable<AppDestination.Home> {
            val viewModel = koinViewModel<HomeViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            HomeScreen(
                state = uiState,
                onAction = viewModel::onAction
            )
        }
        composable<AppDestination.InstanceList> { }
        composable<AppDestination.ProjectList> { }
        composable<AppDestination.Accounts> { }
        composable<AppDestination.Settings> { }
    }
}
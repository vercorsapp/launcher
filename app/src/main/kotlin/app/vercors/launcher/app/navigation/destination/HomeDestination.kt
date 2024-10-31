package app.vercors.launcher.app.navigation.destination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import app.vercors.launcher.app.navigation.AppDestination
import app.vercors.launcher.home.presentation.action.HomeAction
import app.vercors.launcher.home.presentation.ui.HomeScreen
import app.vercors.launcher.home.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeDestination(
    backStackEntry: NavBackStackEntry,
    navController: NavHostController
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        state = uiState,
        onAction = {
            when (it) {
                is HomeAction.Nav.InstallProject -> TODO()
                is HomeAction.Nav.LaunchInstance -> TODO()
                is HomeAction.Nav.ShowInstance -> navController.navigate(AppDestination.InstanceDetails(it.instanceId))
                is HomeAction.Nav.ShowProject -> navController.navigate(AppDestination.ProjectDetails(it.projectId))
                else -> viewModel.onAction(it)
            }
        }
    )
}
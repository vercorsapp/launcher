package app.vercors.launcher.home.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.vercors.launcher.home.presentation.ui.HomeScreen
import app.vercors.launcher.home.presentation.viewmodel.HomeUiEffect
import app.vercors.launcher.instance.domain.model.InstanceId
import app.vercors.launcher.project.domain.model.ProjectId
import kotlinx.serialization.Serializable

@Serializable
internal data object HomeRoute

@Serializable
data object HomeBaseRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(
    route = HomeRoute,
    navOptions = navOptions
)

fun NavGraphBuilder.homeSection(
    onProjectClick: (ProjectId) -> Unit,
    onProjectAction: (ProjectId) -> Unit,
    onInstanceClick: (InstanceId) -> Unit,
    onInstanceAction: (InstanceId) -> Unit,
    onCreateInstanceClick: () -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen {
                when (it) {
                    is HomeUiEffect.NavigateToInstance -> onInstanceClick(it.instanceId)
                    is HomeUiEffect.NavigateToProject -> onProjectClick(it.projectId)
                    HomeUiEffect.CreateInstance -> onCreateInstanceClick()
                }
            }
        }
    }
}
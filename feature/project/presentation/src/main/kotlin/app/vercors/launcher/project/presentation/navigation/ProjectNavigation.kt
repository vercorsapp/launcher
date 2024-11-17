package app.vercors.launcher.project.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.vercors.launcher.project.presentation.list.ProjectListScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProjectBaseRoute

@Serializable
internal data object ProjectListRoute

fun NavController.navigateToInstanceList(navOptions: NavOptions) = navigate(
    route = ProjectListRoute,
    navOptions = navOptions
)

fun NavGraphBuilder.projectSection() {
    navigation<ProjectBaseRoute>(startDestination = ProjectListRoute) {
        composable<ProjectListRoute> {
            ProjectListScreen()
        }
    }
}
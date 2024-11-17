package app.vercors.launcher.instance.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.vercors.launcher.instance.presentation.list.InstanceListScreen
import kotlinx.serialization.Serializable

@Serializable
data object InstanceBaseRoute

@Serializable
internal data object InstanceListRoute

fun NavController.navigateToInstanceList(navOptions: NavOptions) = navigate(
    route = InstanceListRoute,
    navOptions = navOptions
)

fun NavGraphBuilder.instanceSection() {
    navigation<InstanceBaseRoute>(startDestination = InstanceListRoute) {
        composable<InstanceListRoute> {
            InstanceListScreen()
        }
    }
}
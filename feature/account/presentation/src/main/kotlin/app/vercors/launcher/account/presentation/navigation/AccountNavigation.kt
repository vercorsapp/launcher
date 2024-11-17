package app.vercors.launcher.account.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.vercors.launcher.account.presentation.list.AccountListScreen
import kotlinx.serialization.Serializable

@Serializable
data object AccountBaseRoute

@Serializable
internal data object AccountListRoute

fun NavController.navigateToInstanceList(navOptions: NavOptions) = navigate(
    route = AccountListRoute,
    navOptions = navOptions
)

fun NavGraphBuilder.accountSection() {
    navigation<AccountBaseRoute>(startDestination = AccountListRoute) {
        composable<AccountListRoute> {
            AccountListScreen()
        }
    }
}
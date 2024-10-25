package app.vercors.launcher.core.presentation.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface Navigator {
    val startDestination: AppDestination
    val navigationActions: Flow<NavigationAction>

    suspend fun navigateTo(destination: AppDestination)
    suspend fun navigateBack()
}

class DefaultNavigator(
    override val startDestination: AppDestination
) : Navigator {
    private val _navigationActions = Channel<NavigationAction>()
    override val navigationActions = _navigationActions.receiveAsFlow()

    override suspend fun navigateTo(destination: AppDestination) {
        _navigationActions.send(NavigationAction.NavigateTo(destination))
    }

    override suspend fun navigateBack() {
        _navigationActions.send(NavigationAction.NavigateBack)
    }
}

sealed interface NavigationAction {
    data class NavigateTo(val destination: AppDestination) : NavigationAction
    data object NavigateBack : NavigationAction
}
package app.vercors.launcher.core.presentation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface Navigator {
    val startDestination: AppDestination
    val navigationActions: SharedFlow<NavigationAction>

    suspend fun navigateTo(destination: AppDestination)
    suspend fun navigateBack()
}

class DefaultNavigator(
    override val startDestination: AppDestination
) : Navigator {
    private val _navigationActions = MutableSharedFlow<NavigationAction>()
    override val navigationActions = _navigationActions.asSharedFlow()

    override suspend fun navigateTo(destination: AppDestination) {
        _navigationActions.emit(NavigationAction.NavigateTo(destination))
    }

    override suspend fun navigateBack() {
        _navigationActions.emit(NavigationAction.NavigateBack)
    }
}

sealed interface NavigationAction {
    data class NavigateTo(val destination: AppDestination) : NavigationAction
    data object NavigateBack : NavigationAction
}
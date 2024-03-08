package app.vercors.toolbar

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.navigation.NavigationConfig
import app.vercors.navigation.NavigationService
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToolbarComponentImpl(
    componentContext: AppComponentContext,
    override val onToolbarClick: (ToolbarButton) -> Unit,
    private val navigationService: NavigationService = componentContext.inject()
) : AbstractAppComponent(componentContext), ToolbarComponent {
    private val _uiState = MutableStateFlow(ToolbarUiState())
    override val uiState: StateFlow<ToolbarUiState> = _uiState

    override fun onCreate() {
        launch {
            navigationService.navigationState.collect { state ->
                _uiState.update {
                    it.copy(
                        title = persistentListOf(state.active),
                        hasPreviousScreen = state.hasPreviousScreen,
                        hasNextScreen = state.hasNextScreen
                    )
                }
            }
        }
    }

    override fun onTitleClick(config: NavigationConfig) {
        navigationService.navigateTo(config)
    }
}
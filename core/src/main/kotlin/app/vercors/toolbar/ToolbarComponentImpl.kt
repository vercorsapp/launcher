package app.vercors.toolbar

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ToolbarComponentImpl(
    componentContext: AppComponentContext,
    override val onToolbarClick: (ToolbarButton) -> Unit
) : AbstractAppComponent(componentContext), ToolbarComponent {
    private val _uiState = MutableStateFlow(ToolbarUiState())
    override val uiState: StateFlow<ToolbarUiState> = _uiState

    override fun updateState(
        title: ToolbarTitle,
        hasPreviousScreen: Boolean,
        hasNextScreen: Boolean,
        canRefreshScreen: Boolean
    ) {
        _uiState.update {
            it.copy(
                title = title,
                hasPreviousScreen = hasPreviousScreen,
                hasNextScreen = hasNextScreen,
                canRefreshScreen = canRefreshScreen
            )
        }
    }
}
package app.vercors.toolbar

import kotlinx.coroutines.flow.StateFlow

interface ToolbarComponent {
    val uiState: StateFlow<ToolbarUiState>
    val onToolbarClick: (ToolbarButton) -> Unit
    fun updateState(title: ToolbarTitle, hasPreviousScreen: Boolean, hasNextScreen: Boolean, canRefreshScreen: Boolean)
}
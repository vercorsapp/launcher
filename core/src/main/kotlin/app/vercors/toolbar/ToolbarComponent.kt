package app.vercors.toolbar

import app.vercors.navigation.NavigationConfig
import kotlinx.coroutines.flow.StateFlow

interface ToolbarComponent {
    val uiState: StateFlow<ToolbarUiState>
    val onToolbarClick: (ToolbarButton) -> Unit
    val onNotificationButtonClick: () -> Unit
    fun onTitleClick(config: NavigationConfig)
}
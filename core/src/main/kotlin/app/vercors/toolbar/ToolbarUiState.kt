package app.vercors.toolbar

import app.vercors.navigation.NavigationConfig

data class ToolbarUiState(
    val title: List<NavigationConfig> = emptyList(),
    val hasPreviousScreen: Boolean = false,
    val hasNextScreen: Boolean = false,
    val canRefreshScreen: Boolean = true
)

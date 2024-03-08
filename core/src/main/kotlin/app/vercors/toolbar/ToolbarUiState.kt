package app.vercors.toolbar

import app.vercors.navigation.NavigationConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ToolbarUiState(
    val title: ImmutableList<NavigationConfig> = persistentListOf(),
    val hasPreviousScreen: Boolean = false,
    val hasNextScreen: Boolean = false,
    val canRefreshScreen: Boolean = true
)

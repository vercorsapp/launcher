package app.vercors.toolbar

data class ToolbarUiState(
    val title: ToolbarTitle? = null,
    val hasPreviousScreen: Boolean = false,
    val hasNextScreen: Boolean = false,
    val canRefreshScreen: Boolean = false
)

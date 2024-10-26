package app.vercors.launcher.home.presentation.model

sealed interface HomeInstanceStatusUi {
    data class NotRunning(val lastPlayed: String) : HomeInstanceStatusUi
    data object Running : HomeInstanceStatusUi
}
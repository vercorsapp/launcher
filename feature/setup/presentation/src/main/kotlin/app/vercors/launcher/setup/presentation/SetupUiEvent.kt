package app.vercors.launcher.setup.presentation

sealed interface SetupUiEvent {
    data class PickDirectory(val path: String) : SetupUiEvent
    data class UpdatePath(val path: String) : SetupUiEvent
    data object StartApp : SetupUiEvent
}
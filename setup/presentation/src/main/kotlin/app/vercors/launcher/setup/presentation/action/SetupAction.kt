package app.vercors.launcher.setup.presentation.action

sealed interface SetupAction {
    data class PickDirectory(val path: String) : SetupAction
    data class UpdatePath(val path: String) : SetupAction
    data object StartApp : SetupAction
}

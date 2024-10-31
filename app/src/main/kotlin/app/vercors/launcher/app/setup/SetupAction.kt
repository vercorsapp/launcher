package app.vercors.launcher.app.setup

sealed interface SetupAction {
    data class PickDirectory(val path: String) : SetupAction
    data class UpdatePath(val path: String) : SetupAction
    data object StartApp : SetupAction
}

package app.vercors.launcher.app.presentation.state

sealed interface AppDialog {
    data object CreateInstance : AppDialog
    data object AddAccount : AppDialog
}

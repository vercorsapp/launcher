package app.vercors.launcher.app.state

sealed interface AppDialog {
    data object CreateInstance : AppDialog
    data object AddAccount : AppDialog
}

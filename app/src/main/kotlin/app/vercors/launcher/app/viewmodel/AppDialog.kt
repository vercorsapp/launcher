package app.vercors.launcher.app.viewmodel

sealed interface AppDialog {
    data object CreateInstance : AppDialog
    data object AddAccount : AppDialog
}

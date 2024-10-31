package app.vercors.launcher.app.presentation.action

sealed interface MenuBarAction {
    data object Minimize : MenuBarAction
    data object Maximize : MenuBarAction
    data object Close : MenuBarAction
}
package app.vercors.launcher.app.action

sealed interface MenuBarAction {
    data object Minimize : MenuBarAction
    data object Maximize : MenuBarAction
    data object Close : MenuBarAction
    data object Back : MenuBarAction
}
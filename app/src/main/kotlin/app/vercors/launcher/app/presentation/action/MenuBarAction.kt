package app.vercors.launcher.app.presentation.action

sealed interface MenuBarAction : AppAction {
    data class SearchQueryChange(val query: String) : MenuBarAction
    data object Minimize : MenuBarAction
    data object Maximize : MenuBarAction
    data object Close : MenuBarAction
}
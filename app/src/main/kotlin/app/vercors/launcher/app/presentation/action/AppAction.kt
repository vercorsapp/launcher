package app.vercors.launcher.app.presentation.action

sealed interface AppAction {
    data class SearchQueryChange(val query: String) : AppAction
}
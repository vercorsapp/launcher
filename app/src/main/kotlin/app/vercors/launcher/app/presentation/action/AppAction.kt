package app.vercors.launcher.app.presentation.action

import app.vercors.launcher.app.presentation.state.AppDialog

sealed interface AppAction {
    data class OpenDialog(val dialog: AppDialog) : AppAction
    data object CloseDialog : AppAction
}
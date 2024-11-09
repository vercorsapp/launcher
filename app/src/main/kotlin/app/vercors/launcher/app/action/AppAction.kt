package app.vercors.launcher.app.action

import app.vercors.launcher.app.state.AppDialog

sealed interface AppAction {
    data class OpenDialog(val dialog: AppDialog) : AppAction
    data object CloseDialog : AppAction
}
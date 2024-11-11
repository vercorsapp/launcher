package app.vercors.launcher.app.viewmodel

sealed interface AppUiIntent : AppUiEvent {
    @JvmInline
    value class OpenDialog(val dialog: AppDialog) : AppUiIntent
    data object CloseDialog : AppUiIntent
}
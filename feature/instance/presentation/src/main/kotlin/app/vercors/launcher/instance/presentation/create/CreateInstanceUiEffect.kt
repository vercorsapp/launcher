package app.vercors.launcher.instance.presentation.create

sealed interface CreateInstanceUiEffect {
    data object CloseDialog : CreateInstanceUiEffect
}

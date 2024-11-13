package app.vercors.launcher.setup.presentation.viewmodel

sealed interface SetupUiEffect {
    data object Launch : SetupUiEffect
}

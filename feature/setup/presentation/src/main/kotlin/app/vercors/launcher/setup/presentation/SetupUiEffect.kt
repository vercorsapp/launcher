package app.vercors.launcher.setup.presentation

sealed interface SetupUiEffect {
    data object Launch : SetupUiEffect
}

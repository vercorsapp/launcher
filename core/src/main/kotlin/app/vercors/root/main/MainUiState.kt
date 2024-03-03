package app.vercors.root.main

import app.vercors.common.AppPalette
import app.vercors.configuration.ConfigurationData
import java.nio.file.Path

sealed interface MainUiState {
    data object NotLoaded : MainUiState
    data class Loaded(
        val config: ConfigurationData,
        val palette: AppPalette,
        val cachePath: Path,
        val windowEvent: MainWindowEvent?
    ) : MainUiState
}

package app.vercors.launcher.settings.presentation.state

import app.vercors.launcher.core.config.model.AppConfig

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Loaded(
        val config: AppConfig,
        val themes: List<ThemeUi> = ThemeUi.DEFAULT_LIST,
        val accentColors: List<AccentColorUi> = AccentColorUi.DEFAULT_LIST,
    ) : SettingsUiState {
        val currentTheme: ThemeUi = themes.first { it.id == config.general.theme }
        val currentAccentColor: AccentColorUi = accentColors.first { it.id == config.general.accent }
    }
}

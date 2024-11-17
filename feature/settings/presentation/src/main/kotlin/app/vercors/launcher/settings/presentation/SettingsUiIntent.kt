package app.vercors.launcher.settings.presentation

import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.model.TabConfig

sealed interface SettingsUiIntent {
    @JvmInline
    value class SelectTheme(val value: String) : SettingsUiIntent

    @JvmInline
    value class SelectAccent(val value: String) : SettingsUiIntent

    @JvmInline
    value class ToggleGradient(val value: Boolean) : SettingsUiIntent

    @JvmInline
    value class ToggleDecorated(val value: Boolean) : SettingsUiIntent

    @JvmInline
    value class ToggleAnimations(val value: Boolean) : SettingsUiIntent

    @JvmInline
    value class SelectDefaultTab(val value: TabConfig) : SettingsUiIntent

    @JvmInline
    value class ToggleSection(val value: HomeSectionConfig) : SettingsUiIntent

    @JvmInline
    value class SelectProvider(val value: HomeProviderConfig) : SettingsUiIntent
}
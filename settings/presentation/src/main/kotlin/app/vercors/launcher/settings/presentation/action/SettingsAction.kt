package app.vercors.launcher.settings.presentation.action

import app.vercors.launcher.core.config.model.TabConfig

sealed interface SettingsAction {
    @JvmInline
    value class SelectTheme(val value: String) : SettingsAction

    @JvmInline
    value class SelectAccent(val value: String) : SettingsAction

    @JvmInline
    value class ToggleDecorated(val value: Boolean) : SettingsAction

    @JvmInline
    value class ToggleAnimations(val value: Boolean) : SettingsAction

    @JvmInline
    value class SelectDefaultTab(val value: TabConfig) : SettingsAction
}
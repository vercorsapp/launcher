package app.vercors.launcher.settings.presentation.viewmodel

import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig

sealed interface SettingsUiEvent {
    @JvmInline
    value class ConfigUpdated(val config: AppConfig) : SettingsUiEvent

    @JvmInline
    value class UpdateSections(val value: List<HomeSectionConfig>) : SettingsUiEvent
}
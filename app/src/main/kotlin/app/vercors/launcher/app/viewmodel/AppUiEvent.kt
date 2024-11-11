package app.vercors.launcher.app.viewmodel

import app.vercors.launcher.core.config.model.AppConfig

sealed interface AppUiEvent {
    @JvmInline
    value class ConfigUpdated(val config: AppConfig) : AppUiEvent
}

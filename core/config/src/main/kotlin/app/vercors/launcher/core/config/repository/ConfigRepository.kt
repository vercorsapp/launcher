package app.vercors.launcher.core.config.repository

import app.vercors.launcher.core.config.model.AppConfig
import kotlinx.coroutines.flow.StateFlow

interface ConfigRepository {
    val configState: StateFlow<AppConfig>
    suspend fun updateConfig(update: (AppConfig) -> AppConfig)
}

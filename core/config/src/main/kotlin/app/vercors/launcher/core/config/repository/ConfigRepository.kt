package app.vercors.launcher.core.config.repository

import app.vercors.launcher.core.config.model.AppConfig
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun observeConfig(): Flow<AppConfig>
    suspend fun <T> updateConfig(update: ConfigUpdate<T>, value: T)
}

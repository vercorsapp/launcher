package app.vercors.launcher.core.config.repository

import androidx.datastore.core.DataStore
import app.vercors.launcher.core.config.mapper.toConfig
import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.proto.ConfigProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ConfigRepositoryImpl(
    private val dataStore: DataStore<ConfigProto>
) : ConfigRepository {
    override fun observeConfig(): Flow<AppConfig> = dataStore.data.map { it.toConfig() }

    override suspend fun <T> updateConfig(update: ConfigUpdate<T>, value: T) {
        dataStore.updateData { it.update(value) }
    }
}
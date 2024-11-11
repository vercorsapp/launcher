package app.vercors.launcher.core.config.repository

import androidx.datastore.core.DataStore
import app.vercors.launcher.core.config.mapper.toConfig
import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.proto.ConfigProto
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {}

@Single
class ConfigRepositoryImpl(
    private val dataStore: DataStore<ConfigProto>,
    @Named("externalScope") private val externalScope: CoroutineScope,
) : ConfigRepository {
    private val _configSharedFlow = dataStore.data
        .map { it.toConfig() }
        .onEach { logger.debug { "Configuration updated: $it" } }
        .shareIn(externalScope, SharingStarted.Eagerly, 1)

    override fun observeConfig(): Flow<AppConfig> = _configSharedFlow

    override suspend fun <T> updateConfig(update: ConfigUpdate<T>, value: T) {
        logger.debug { "Updating Config: $update = $value" }
        dataStore.updateData { update.updater(it, value) }
    }
}
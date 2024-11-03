package app.vercors.launcher.core.config.repository

import androidx.datastore.core.DataStore
import app.vercors.launcher.core.config.mapper.AppConfigMapper
import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.proto.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class ConfigRepositoryImpl(
    @Named("externalScope") private val externalScope: CoroutineScope,
    private val dataStore: DataStore<Config>,
    private val appConfigMapper: AppConfigMapper
) : ConfigRepository {
    override val configState: StateFlow<AppConfig> = dataStore.data
        .map { appConfigMapper.fromProto(it) }
        .stateIn(externalScope, SharingStarted.WhileSubscribed(3000), AppConfig.DEFAULT)

    override suspend fun updateConfig(update: (AppConfig) -> AppConfig) {
        dataStore.updateData { appConfigMapper.toProto(update(appConfigMapper.fromProto(it))) }
    }
}
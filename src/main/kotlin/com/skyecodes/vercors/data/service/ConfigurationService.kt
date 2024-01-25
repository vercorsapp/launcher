package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.data.model.app.Configuration
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

private val logger = KotlinLogging.logger { }

interface ConfigurationService {
    val state: StateFlow<ConfigurationState>
    val config: Flow<Configuration>
    fun load(): Job
    fun update(config: Configuration)
}

sealed interface ConfigurationState {
    data object NotLoaded : ConfigurationState
    data class Errored(val exception: ConfigurationLoadingException) : ConfigurationState
    data class Loaded(val config: Configuration) : ConfigurationState
}

class ConfigurationLoadingException(message: String, cause: Throwable) : Exception(message, cause)

class ConfigurationServiceImpl(
    coroutineScope: CoroutineScope,
    storageService: StorageService,
    private val json: Json,
    private val configFile: Path = storageService.configDir.resolve("settings.json")
) : ConfigurationService, CoroutineScope by coroutineScope {
    override var state = MutableStateFlow<ConfigurationState>(ConfigurationState.NotLoaded)
    override val config = state.filterIsInstance<ConfigurationState.Loaded>().map { it.config }

    init {
        coroutineScope.launch { config.drop(1).collect { save(it) } }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun load() = launch(Dispatchers.IO) {
        logger.debug { "Loading configuration at location $configFile" }
        if (configFile.exists()) {
            try {
                update(configFile.inputStream().use { json.decodeFromStream(it) })
            } catch (e: SerializationException) {
                val message =
                    "Unable to load configuration. Please check that the configuration file at location $configFile is valid, or delete it to recreate it when you restart the application."
                logger.error(e) { message }
                state.value = ConfigurationState.Errored(ConfigurationLoadingException(message, e))
            }
        } else {
            update(Configuration.DEFAULT)
            save(Configuration.DEFAULT)
        }
        logger.info { "Configuration loaded" }
    }

    override fun update(config: Configuration) {
        state.value = ConfigurationState.Loaded(config)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun save(config: Configuration) = withContext(Dispatchers.IO) {
        configFile.createParentDirectories().outputStream().use { json.encodeToStream(config, it) }
        logger.info { "Configuration saved" }
    }
}
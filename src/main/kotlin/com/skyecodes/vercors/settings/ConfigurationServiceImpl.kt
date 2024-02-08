package com.skyecodes.vercors.settings

import com.skyecodes.vercors.common.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    override fun loadConfiguration() = launch(Dispatchers.IO) {
        logger.debug { "Loading configuration at location $configFile" }
        if (configFile.exists()) {
            try {
                updateConfiguration(configFile.inputStream().use { json.decodeFromStream(it) })
            } catch (e: SerializationException) {
                val message =
                    "Unable to load configuration. Please check that the configuration file at location $configFile is valid, or delete it to recreate it when you restart the application."
                logger.error(e) { message }
                state.value = ConfigurationState.Errored(ConfigurationLoadingException(message, e))
            }
        } else {
            updateConfiguration(Configuration.DEFAULT)
            save(Configuration.DEFAULT)
        }
        logger.info { "Configuration loaded" }
    }

    override fun updateConfiguration(config: Configuration) {
        state.value = ConfigurationState.Loaded(config)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun save(config: Configuration) = withContext(Dispatchers.IO) {
        configFile.createParentDirectories().outputStream().use { json.encodeToStream(config, it) }
        logger.info { "Configuration saved" }
    }
}
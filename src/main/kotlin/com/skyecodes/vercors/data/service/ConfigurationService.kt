package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.appJson
import com.skyecodes.vercors.data.model.app.Configuration
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

interface ConfigurationService {
    suspend fun load(): Configuration
    suspend fun save(config: Configuration)
}

class ConfigurationLoadingException(message: String, cause: Throwable) : Exception(message, cause)

private val logger = KotlinLogging.logger { }

class ConfigurationServiceImpl(
    coroutineScope: CoroutineScope,
    storageService: StorageService
) : ConfigurationService, CoroutineScope by coroutineScope {
    private val configFile = storageService.configDir.resolve("settings.json")

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun load(): Configuration = withContext(Dispatchers.IO) {
        var config = Configuration.DEFAULT
        if (configFile.exists()) {
            try {
                config = configFile.inputStream().use { appJson.decodeFromStream(it) }
            } catch (e: SerializationException) {
                val message =
                    "Unable to load configuration. Please check that the configuration file at location $configFile is valid, or delete it to recreate it when you restart the application."
                logger.error(e) { message }
                throw ConfigurationLoadingException(message, e)
            }
        } else {
            save(config)
        }
        logger.info { "Configuration loaded" }
        config
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun save(config: Configuration) = withContext(Dispatchers.IO) {
        configFile.createParentDirectories().outputStream().use { appJson.encodeToStream(config, it) }
        logger.info { "Configuration saved" }
    }
}
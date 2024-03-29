package app.vercors.configuration

import app.vercors.system.storage.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlin.io.path.*

private val logger = KotlinLogging.logger { }

class ConfigurationRepositoryImpl(
    private val json: Json,
    private val storageService: StorageService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ConfigurationRepository {
    override suspend fun load(): ConfigurationData = withContext(dispatcher) {
        val path = storageService.configPath
        try {
            if (path.isRegularFile()) {
                logger.info { "Configuration file found at location $path" }
                return@withContext path.inputStream().use { json.decodeFromStream(it) }
            } else if (path.exists()) {
                throw ConfigurationRepositoryException("Configuration file path $path is not empty")
            }
            logger.info { "Configuration file not found - initializing default configuration file" }
            val config = defaultConfig
            save(config)
            config
        } catch (e: Exception) {
            if (e is ConfigurationRepositoryException) throw e
            throw ConfigurationRepositoryException("Unable to load configuration", e)
        }
    }

    override suspend fun save(config: ConfigurationData) {
        withContext(dispatcher) {
            val path = storageService.configPath
            try {
                logger.debug { "Writing configuration file at location $path" }
                path.createParentDirectories().outputStream().use { json.encodeToStream(config, it) }
            } catch (e: Exception) {
                throw ConfigurationRepositoryException("Unable to save configuration", e)
            }
        }
    }

    override val defaultConfig: ConfigurationData = ConfigurationData.DEFAULT
}
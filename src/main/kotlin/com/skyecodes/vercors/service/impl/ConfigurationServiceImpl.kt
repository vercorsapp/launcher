package com.skyecodes.vercors.service.impl

import com.skyecodes.vercors.appJson
import com.skyecodes.vercors.data.app.Configuration
import com.skyecodes.vercors.service.ConfigurationService
import com.skyecodes.vercors.service.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

private val logger = KotlinLogging.logger { }

class ConfigurationServiceImpl(storageService: StorageService) : ConfigurationService {
    private val configFile = storageService.configDir.resolve("settings.json")

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun load(): Configuration = withContext(Dispatchers.IO) {
        var config = Configuration.DEFAULT
        if (configFile.exists()) {
            config = configFile.inputStream().use { appJson.decodeFromStream(it) }
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
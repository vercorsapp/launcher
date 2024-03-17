/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
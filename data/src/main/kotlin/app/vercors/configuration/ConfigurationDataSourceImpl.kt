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

import app.vercors.system.StorageManager
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

private val logger = KotlinLogging.logger {}

internal class ConfigurationDataSourceImpl(
    private val json: Json,
    private val storageManager: StorageManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ConfigurationDataSource {
    override suspend fun loadConfiguration(): Configuration = withContext(ioDispatcher) {
        val configurationFilePath = storageManager.configPath
        try {
            if (configurationFilePath.exists()) {
                logger.debug { "Configuration file found at location $configurationFilePath" }
                configurationFilePath.inputStream().use { json.decodeFromStream(it) }
            } else {
                logger.debug { "Configuration file not found at location $configurationFilePath - initializing default configuration file" }
                Configuration.Default.also { saveConfiguration(it) }
            }
        } catch (e: Exception) {
            throw ConfigurationIOException("An error occured while loading configuration", e)
        }
    }

    override suspend fun saveConfiguration(configuration: Configuration) = withContext(ioDispatcher) {
        val configurationFilePath = storageManager.configPath
        try {
            logger.debug { "Writing configuration file at location $configurationFilePath" }
            configurationFilePath.createParentDirectories().outputStream()
                .use { json.encodeToStream(configuration, it) }
        } catch (e: Exception) {
            throw ConfigurationIOException("An error occured while saving configuration", e)
        }
    }
}
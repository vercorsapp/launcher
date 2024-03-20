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

package app.vercors.instance

import app.vercors.system.StorageManager
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicLong
import kotlin.io.path.*
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger { }

internal class InstanceDataSourceImpl(
    private val json: Json,
    private val storageManager: StorageManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : InstanceDataSource {
    override fun loadInstances(): Flow<Pair<Float, InstanceResult>> = channelFlow {
        val instanceDirPath = storageManager.instancesPath
        logger.info { "Loading instances" }
        measureTimeMillis {
            val instanceDirs = Files.list(instanceDirPath.createDirectories())
                .filter { it.isDirectory() && it.parent.absolutePathString() == instanceDirPath.absolutePathString() }
                .map { it.name }
                .toList()
            val maxCount = instanceDirs.count().toFloat()
            val count = AtomicLong(0)
            instanceDirs.map {
                launch(ioDispatcher) {
                    try {
                        val instance = loadInstance(it)
                        send(count.incrementAndGet() / maxCount to instance)
                    } catch (e: Exception) {
                        send(count.incrementAndGet() / maxCount to InstanceResult.Error(e))
                    }
                }
            }.joinAll()
        }.let { logger.info { "Loaded instances in ${it}ms" } }
    }

    private suspend fun loadInstance(id: String): InstanceResult {
        try {
            val jsonPath = storageManager.getInstanceConfigPath(id)
            logger.debug { "Loading instance at location $jsonPath" }
            var instance = jsonPath.inputStream().use { json.decodeFromStream<InstanceData>(it) }
            if (instance.id != id) {
                logger.warn { "Directory of instance ${instance.name} changed to $id, patching file data" }
                instance = instance.copy(id = id)
                saveInstance(instance)
            }
            logger.debug { "Loaded instance ${instance.name}" }
            return InstanceResult.Success(instance)
        } catch (e: Exception) {
            throw InstanceIOException("An error occured while loading instance", e)
        }
    }

    override suspend fun saveInstance(instance: InstanceData) = withContext(ioDispatcher) {
        try {
            var instanceWithId = instance
            if (instance.id.isBlank()) {
                instanceWithId = instance.copy(id = generateInstanceId(storageManager.instancesPath, instance))
            }
            val instanceConfigPath = storageManager.getInstanceConfigPath(instanceWithId.id)
            instanceConfigPath.createParentDirectories().outputStream().use { json.encodeToStream(instanceWithId, it) }
            instanceWithId
        } catch (e: Exception) {
            throw InstanceIOException("An error occured while saving instance", e)
        }
    }

    private fun generateInstanceId(instanceDirPath: Path, instance: InstanceData): String {
        val sanitizedName = instance.name.replace("[^a-zA-Z0-9._]+".toRegex(), "_")
        var path = instanceDirPath.resolve(sanitizedName)
        var suffix = 1
        while (path.exists()) path = instanceDirPath.resolve("${sanitizedName}_${suffix++}")
        return path.name
    }
}
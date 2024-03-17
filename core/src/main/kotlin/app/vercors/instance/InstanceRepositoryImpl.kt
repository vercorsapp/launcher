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

import app.vercors.system.storage.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.*
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger { }

class InstanceRepositoryImpl(
    private val json: Json,
    private val storageService: StorageService,
    private val instanceFileName: String = "instance.json"
) : InstanceRepository {
    val path get() = storageService.instancesPath

    override fun loadInstances(maximumParallelThreads: Int, context: CoroutineContext): Flow<InstanceLoadingResult> =
        channelFlow {
            val dispatcher = Dispatchers.IO.limitedParallelism(maximumParallelThreads) + SupervisorJob()
            logger.info { "Loading instances" }
            measureTimeMillis {
                Files.list(path.createDirectories()).map { instanceDir ->
                    launch(dispatcher) {
                        loadInstance(instanceDir)?.let { send(it) }
                    }
                }.toList().joinAll()
            }.let { logger.info { "Loaded instances in ${it}ms" } }
        }.flowOn(context)

    private suspend fun loadInstance(instanceDir: Path): InstanceLoadingResult? =
        coroutineScope {
            runCatching {
                if (instanceDir != path) {
                    val jsonFile = instanceDir.resolve(instanceFileName)
                    return@runCatching if (jsonFile.isRegularFile()) {
                        loadInstanceFromFile(instanceDir, jsonFile)
                    } else {
                        logger.warn { "Couldn't find the $instanceFileName file at location $jsonFile" }
                        InstanceLoadingResult.Warn(instanceDir)
                    }
                }
                null
            }.getOrElse { InstanceLoadingResult.Error(it) }
        }

    private suspend fun loadInstanceFromFile(instanceDir: Path, jsonFile: Path): InstanceLoadingResult.Success {
        try {
            logger.debug { "Loading instance at location $jsonFile" }
            var instance = jsonFile.inputStream().use { json.decodeFromStream<InstanceData>(it) }
            if (instance.id != instanceDir.name) {
                logger.warn { "Directory of instance ${instance.name} changed to ${instanceDir.name}, patching file data" }
                instance = instance.copy(id = instanceDir.name)
                saveInstance(instance)
            }
            logger.debug { "Loaded instance ${instance.name}" }
            return InstanceLoadingResult.Success(instance.copy(id = instanceDir.name))
        } catch (e: Exception) {
            throw InstanceLoadingException("There was an error while loading instance at location $jsonFile", e)
        }
    }

    override suspend fun saveInstance(instance: InstanceData, log: Boolean, context: CoroutineContext) =
        withContext(context) {
            val path = storageService.getInstancePath(instance)
            if (log) logger.debug { "Saving instance ${instance.name} at location $path" }
        path.resolve(instanceFileName).createParentDirectories()
            .outputStream().use { json.encodeToStream(instance, it) }
            if (log) logger.debug { "Saved instance ${instance.name}" }
    }

    override suspend fun deleteInstance(instance: InstanceData, context: CoroutineContext) = withContext(context) {
        logger.debug { "Deleting instance ${instance.name}" }
        storageService.getInstancePath(instance).deleteRecursively()
        logger.debug { "Deleted instance ${instance.name}" }
    }
}
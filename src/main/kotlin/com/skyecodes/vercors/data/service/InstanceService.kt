package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.appJson
import com.skyecodes.vercors.data.model.app.Instance
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Files
import kotlin.io.path.*

interface InstanceService {
    suspend fun loadInstances(): List<Instance>
    suspend fun createInstance(instance: Instance)
    suspend fun deleteInstance(instance: Instance)
}

class InstanceLoadingException(message: String, cause: Throwable) : Exception(message, cause)

private val logger = KotlinLogging.logger { }

class InstanceServiceImpl(
    coroutineScope: CoroutineScope,
    private val storageService: StorageService
) : InstanceService, CoroutineScope by coroutineScope {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun loadInstances(): List<Instance> = withContext(Dispatchers.IO) {
        Files.list(storageService.instancesDir.createDirectories()).map { instanceDir ->
            async {
                if (instanceDir != storageService.instancesDir) {
                    val jsonFile = instanceDir.resolve("instance.json")
                    if (jsonFile.isRegularFile()) {
                        try {
                            logger.debug { "Loading instance at location $jsonFile" }
                            return@async jsonFile.inputStream().use { appJson.decodeFromStream<Instance>(it) }
                                .also { logger.debug { "Loaded instance ${it.name}" } }
                        } catch (e: Exception) {
                            val message = "There was an error while loading instance at location $jsonFile"
                            logger.error(e) { message }
                            throw InstanceLoadingException(message, e)
                        }
                    } else {
                        logger.warn { "Couldn't find the instance.json file at location $jsonFile" }
                    }
                }
                null
            }
        }.toList().awaitAll().filterNotNull()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun createInstance(instance: Instance) = withContext(Dispatchers.IO) {
        val sanitizedName = instance.name.filter { it !in "/\\" }
        var path = storageService.instancesDir.resolve(sanitizedName)
        var suffix = 1
        while (path.exists()) {
            path = storageService.instancesDir.resolve("${sanitizedName}_${suffix++}")
        }
        instance.path = path.name
        path.resolve("instance.json").createParentDirectories().outputStream()
            .use { appJson.encodeToStream(instance, it) }
    }

    @OptIn(ExperimentalPathApi::class)
    override suspend fun deleteInstance(instance: Instance) = withContext(Dispatchers.IO) {
        storageService.instancesDir.resolve(instance.name).deleteRecursively()
    }
}
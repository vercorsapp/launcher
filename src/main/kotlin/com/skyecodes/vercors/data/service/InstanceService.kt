package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.appJson
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Files
import java.time.Instant
import kotlin.io.path.*

interface InstanceService {
    suspend fun loadInstances(scope: CoroutineScope): List<Instance>

    suspend fun createInstance(
        instanceName: String,
        version: MojangVersionManifest.Version,
        loader: Loader?,
        loaderVersion: String?
    )
}

private val logger = KotlinLogging.logger { }

class InstanceServiceImpl(private val storageService: StorageService) : InstanceService {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun loadInstances(scope: CoroutineScope): List<Instance> =
        withContext(Dispatchers.IO) { Files.list(storageService.instancesDir.createDirectories()) }.map { instanceDir ->
            scope.async {
                withContext(Dispatchers.IO) {
                    if (instanceDir != storageService.instancesDir) {
                        val jsonFile = instanceDir.resolve("instance.json")
                        if (jsonFile.isRegularFile()) {
                            try {
                                logger.debug { "Loading instance at location $jsonFile" }
                                return@withContext jsonFile.inputStream().use { appJson.decodeFromStream<Instance>(it) }
                                    .also { logger.debug { "Loaded instance ${it.name}" } }
                            } catch (e: Exception) {
                                logger.error(e) { "There was an error while loading instance at location $jsonFile" }
                            }
                        } else {
                            logger.warn { "Couldn't find the instance.json file at location $jsonFile" }
                        }
                    }
                    null
                }
            }
        }.toList().awaitAll().filterNotNull()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun createInstance(
        instanceName: String,
        version: MojangVersionManifest.Version,
        loader: Loader?,
        loaderVersion: String?
    ) {
        val instance = Instance(instanceName, version.id, loader, loaderVersion, Instant.now())
        storageService.instancesDir.resolve(instanceName).resolve("instance.json").createParentDirectories()
            .outputStream().use { appJson.encodeToStream(instance, it) }
    }
}
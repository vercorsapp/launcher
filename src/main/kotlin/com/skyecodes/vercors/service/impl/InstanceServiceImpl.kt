package com.skyecodes.vercors.service.impl

import com.skyecodes.vercors.appJson
import com.skyecodes.vercors.data.app.Instance
import com.skyecodes.vercors.service.InstanceService
import com.skyecodes.vercors.service.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Files
import kotlin.io.path.createDirectories
import kotlin.io.path.inputStream
import kotlin.io.path.isRegularFile

private val logger = KotlinLogging.logger { }

class InstanceServiceImpl(private val storageService: StorageService) : InstanceService {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fetchInstances(scope: CoroutineScope): List<Instance> =
        withContext(Dispatchers.IO) { Files.walk(storageService.instancesDir.createDirectories()) }.map { instanceDir ->
            scope.async {
                val jsonFile = instanceDir.resolve("instance.json")
                if (jsonFile.isRegularFile()) {
                    try {
                        return@async jsonFile.inputStream().use { appJson.decodeFromStream<Instance>(it) }
                    } catch (e: Exception) {
                        logger.error(e) { "There was an error while fetching instances" }
                    }
                } else {
                    logger.warn { "Couldn't find the instances.json file" }
                }
                null
            }
        }.toList().awaitAll().filterNotNull()
}
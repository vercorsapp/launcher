package com.skyecodes.snowball.service

import com.skyecodes.snowball.appJson
import com.skyecodes.snowball.data.app.Instance
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Files
import kotlin.io.path.createDirectories
import kotlin.io.path.inputStream
import kotlin.io.path.isRegularFile

private val logger = KotlinLogging.logger { }

object InstanceService {
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchInstances(scope: CoroutineScope): List<Instance> =
        withContext(Dispatchers.IO) { Files.walk(StorageService.instancesDir.createDirectories()) }.map { instanceDir ->
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
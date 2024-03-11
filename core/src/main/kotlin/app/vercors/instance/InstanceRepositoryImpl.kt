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
            if (instance.path != instanceDir.name) {
                logger.warn { "Directory of instance ${instance.name} changed to ${instanceDir.name}, fixing that" }
                instance = instance.copy(path = instanceDir.name)
                saveInstance(instance)
            }
            logger.debug { "Loaded instance ${instance.name}" }
            return InstanceLoadingResult.Success(instance.copy(path = instanceDir.name))
        } catch (e: Exception) {
            throw InstanceLoadingException("There was an error while loading instance at location $jsonFile", e)
        }
    }

    override suspend fun saveInstance(instance: InstanceData, context: CoroutineContext) = withContext(context) {
        var instanceWithPath = instance
        if (instance.path.isEmpty()) instanceWithPath = instance.copy(path = generatePath(instance))
        val path = storageService.getInstancePath(instanceWithPath)
        logger.debug { "Saving instance ${instance.name} at location $path" }
        path.resolve(instanceFileName).createParentDirectories()
            .outputStream().use { json.encodeToStream(instanceWithPath, it) }
        logger.debug { "Saved instance ${instance.name}" }
        instanceWithPath
    }

    private fun generatePath(instance: InstanceData): String {
        val sanitizedName = instance.name.replace("[^a-zA-Z0-9._]+".toRegex(), "_")
        var path = storageService.instancesPath.resolve(sanitizedName)
        var suffix = 1
        while (path.exists()) {
            path = storageService.instancesPath.resolve("${sanitizedName}_${suffix++}")
        }
        return path.name
    }

    override suspend fun deleteInstance(instance: InstanceData, context: CoroutineContext) = withContext(context) {
        logger.debug { "Deleting instance ${instance.name}" }
        storageService.getInstancePath(instance).deleteRecursively()
        logger.debug { "Deleted instance ${instance.name}" }
    }
}
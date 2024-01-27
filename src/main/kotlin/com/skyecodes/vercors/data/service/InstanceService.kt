package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.data.model.app.Instance
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*

private val logger = KotlinLogging.logger { }

interface InstanceService {
    val state: StateFlow<InstancesState>
    val instances: Flow<List<Instance>>
    val warns: Flow<List<InstanceLoadingException>>
    val errors: Flow<List<InstanceLoadingException>>
    fun loadInstances(): Job
    fun reloadInstances(): Job
    fun createInstance(instance: Instance): Deferred<Instance>
    fun deleteInstance(instance: Instance): Job
}

sealed interface InstancesState {
    data object NotLoaded : InstancesState
    data class Loaded(
        val instances: List<Instance> = emptyList(),
        val errors: List<InstanceLoadingException> = emptyList(),
        val warns: List<InstanceLoadingException> = emptyList()
    ) : InstancesState
}

class InstanceLoadingException(message: String, val directory: Path, cause: Throwable? = null) :
    Exception(message, cause)

class InstanceServiceImpl(
    coroutineScope: CoroutineScope,
    private val storageService: StorageService,
    private val json: Json
) : InstanceService, CoroutineScope by coroutineScope {
    override var state = MutableStateFlow<InstancesState>(InstancesState.NotLoaded)
    override val instances = state.filterIsInstance<InstancesState.Loaded>().map { it.instances }
    override val warns = state.filterIsInstance<InstancesState.Loaded>().map { it.warns }
    override val errors = state.filterIsInstance<InstancesState.Loaded>().map { it.errors }

    @OptIn(ExperimentalSerializationApi::class)
    override fun loadInstances() = launch(Dispatchers.IO) {
        Files.list(storageService.instancesDir.createDirectories()).map { instanceDir ->
            launch {
                if (instanceDir != storageService.instancesDir) {
                    val jsonFile = instanceDir.resolve("instance.json")
                    if (jsonFile.isRegularFile()) {
                        try {
                            logger.debug { "Loading instance at location $jsonFile" }
                            val instance = jsonFile.inputStream().use { json.decodeFromStream<Instance>(it) }
                            addInstance(instance)
                            logger.debug { "Loaded instance ${instance.name}" }
                        } catch (e: Exception) {
                            val message = "There was an error while loading instance at location $jsonFile"
                            logger.error(e) { message }
                            addError(InstanceLoadingException(message, instanceDir, e))
                        }
                    } else {
                        val message = "Couldn't find the instance.json file at location $jsonFile"
                        logger.warn { message }
                        addWarn(InstanceLoadingException(message, instanceDir))
                    }
                }
            }
        }.toList().joinAll()
        state.update { if (it is InstancesState.NotLoaded) InstancesState.Loaded() else it }
        logger.info { "Instances loaded" }
    }

    override fun reloadInstances(): Job {
        state.value = InstancesState.Loaded()
        return loadInstances()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun createInstance(instance: Instance) = async(Dispatchers.IO) {
        val sanitizedName = instance.name.filter { it !in "/\\" }
        var path = storageService.instancesDir.resolve(sanitizedName)
        var suffix = 1
        while (path.exists()) {
            path = storageService.instancesDir.resolve("${sanitizedName}_${suffix++}")
        }
        val instanceWithPath = instance.copy(path = path.name)
        path.resolve("instance.json").createParentDirectories().outputStream()
            .use { json.encodeToStream(instanceWithPath, it) }
        addInstance(instanceWithPath)
        logger.debug { "Created instance ${instance.name}" }
        instanceWithPath
    }

    @OptIn(ExperimentalPathApi::class)
    override fun deleteInstance(instance: Instance) = launch(Dispatchers.IO) {
        storageService.instancesDir.resolve(instance.name).deleteRecursively()
        state.update { (it as InstancesState.Loaded).copy(instances = it.instances - instance) }
    }

    private fun addInstance(instance: Instance) {
        state.update {
            when (it) {
                is InstancesState.Loaded -> it.copy(instances = it.instances + instance)
                InstancesState.NotLoaded -> InstancesState.Loaded(instances = listOf(instance))
            }
        }
    }

    private fun addError(error: InstanceLoadingException) {
        state.update {
            when (it) {
                is InstancesState.Loaded -> it.copy(errors = it.errors + error)
                InstancesState.NotLoaded -> InstancesState.Loaded(errors = listOf(error))
            }
        }
    }

    private fun addWarn(warn: InstanceLoadingException) {
        state.update {
            when (it) {
                is InstancesState.Loaded -> it.copy(warns = it.warns + warn)
                InstancesState.NotLoaded -> InstancesState.Loaded(warns = listOf(warn))
            }
        }
    }
}
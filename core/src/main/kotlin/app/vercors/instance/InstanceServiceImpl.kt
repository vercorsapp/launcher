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

import app.vercors.configuration.ConfigurationService
import app.vercors.onSuccess
import app.vercors.system.storage.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import kotlin.io.path.exists
import kotlin.io.path.name

private val logger = KotlinLogging.logger { }

class InstanceServiceImpl(
    coroutineScope: CoroutineScope,
    private val configurationService: ConfigurationService,
    private val repository: InstanceRepository,
    private val storageService: StorageService
) : InstanceService, CoroutineScope by coroutineScope {
    private val _loadingState: MutableStateFlow<InstanceLoadingState> = MutableStateFlow(InstanceLoadingState.NotLoaded)
    override val loadingState: StateFlow<InstanceLoadingState> = _loadingState
    override val instancesState: StateFlow<List<InstanceData>?> = loadingState
        .map {
            when (it) {
                is InstanceLoadingState.Loading -> it.instances
                is InstanceLoadingState.Loaded -> it.instances
                else -> null
            }
        }
        .stateIn(this, SharingStarted.Eagerly, null)
    override val instances: List<InstanceData> get() = instancesState.value!!

    init {
        launch {
            instancesState.filterNotNull().collect { instances ->
                instances.filter { it.dirty }.forEach {
                    repository.saveInstance(it, false)
                    it.dirty = false
                }
            }
        }
    }

    override fun loadInstances(): Job = launch {
        repository.loadInstances(configurationService.config.maximumParallelThreads)
            .onStart {
                _loadingState.update {
                    InstanceLoadingState.Loading()
                }
            }
            .onCompletion { _ ->
                val state = loadingState.value as InstanceLoadingState.Loading
                state.errors.forEach { logger.error(it) { "Error occured while loading instance" } }
                state.warns.forEach { logger.warn { "Could not find instance at location $it" } }
                _loadingState.updateAs<InstanceLoadingState.Loading> {
                    InstanceLoadingState.Loaded(it.instances, it.warns, it.errors)
                }
            }
            .collect { result ->
                _loadingState.updateAs<InstanceLoadingState.Loading> {
                    when (result) {
                        is InstanceLoadingResult.Error -> it.copy(errors = it.errors + result.error)
                        is InstanceLoadingResult.Success -> it.copy(instances = it.instances + result.instance)
                        is InstanceLoadingResult.Warn -> it.copy(warns = it.warns + result.path)
                    }
                }
            }
    }

    override fun createInstance(instance: InstanceData) {
        logger.info { "Creating instance ${instance.name}" }
        val instanceWithId = instance.copy(id = generateInstanceId(instance), dirty = true)
        _loadingState.updateAs<InstanceLoadingState.Loaded> {
            it.copy(instances = it.instances + instanceWithId)
        }
        logger.info { "Created instance ${instance.name}" }
    }

    private fun generateInstanceId(instance: InstanceData): String {
        val sanitizedName = instance.name.replace("[^a-zA-Z0-9._]+".toRegex(), "_")
        var path = storageService.instancesPath.resolve(sanitizedName)
        var suffix = 1
        while (path.exists()) path = storageService.instancesPath.resolve("${sanitizedName}_${suffix++}")
        return path.name
    }

    override fun updateInstance(id: String, update: (InstanceData) -> InstanceData) {
        _loadingState.updateAs<InstanceLoadingState.Loaded> {
            it.copy(instances = it.instances.map { instance ->
                if (instance.id == id) update(instance).apply {
                    dirty = true
                } else instance
            })
        }
    }

    override fun tickRunningInstance(id: String, lastInstant: Instant?) {
        val now = Instant.now()
        updateInstance(id) {
            it.copy(
                status = InstanceStatus.Running,
                lastPlayed = now,
                timePlayed = it.timePlayed + Duration.between(lastInstant ?: it.lastPlayed, now)
            )
        }
    }

    override fun deleteInstance(id: String): Job {
        val instance = findInstance(id)
        logger.info { "Deleting instance ${instance.name}" }
        return launch { repository.deleteInstance(instance) }.onSuccess {
            _loadingState.updateAs<InstanceLoadingState.Loaded> {
                it.copy(instances = it.instances.filter { instance -> instance.id != id })
            }
            logger.info { "Deleted instance ${instance.name}" }
        }
    }

    private fun findInstance(id: String) = instances.first { it.id == id }

    @Suppress("unchecked_cast")
    private fun <T : InstanceLoadingState> MutableStateFlow<InstanceLoadingState>.updateAs(
        function: (T) -> InstanceLoadingState
    ) = update { function(it as T) }
}
package app.vercors.instance

import app.vercors.configuration.ConfigurationService
import app.vercors.onSuccess
import app.vercors.onSuccessUse
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private val logger = KotlinLogging.logger { }

class InstanceServiceImpl(
    coroutineScope: CoroutineScope,
    private val configurationService: ConfigurationService,
    private val repository: InstanceRepository
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun createInstance(instance: InstanceData): Deferred<InstanceData> {
        logger.info { "Creating instance ${instance.name}" }
        return saveInstance(instance).onSuccessUse {
            _loadingState.updateAs<InstanceLoadingState.Loaded> {
                it.copy(instances = it.instances + getCompleted())
            }
            logger.info { "Created instance ${instance.name}" }
        }
    }

    override fun updateInstance(instance: InstanceData): Job {
        logger.info { "Updating instance ${instance.name}" }
        return saveInstance(instance).onSuccessUse {
            _loadingState.updateAs<InstanceLoadingState.Loaded> {
                it.copy(instances = it.instances.map { old -> if (old.path == instance.path) instance else old })
            }
            logger.info { "Updated instance ${instance.name}" }
        }
    }

    override fun deleteInstance(instance: InstanceData): Job {
        logger.info { "Deleting instance ${instance.name}" }
        return launch { repository.deleteInstance(instance) }.onSuccess {
            _loadingState.updateAs<InstanceLoadingState.Loaded> {
                it.copy(instances = it.instances - instance)
            }
            logger.info { "Deleted instance ${instance.name}" }
        }
    }

    private fun saveInstance(instance: InstanceData) = async { repository.saveInstance(instance) }

    @Suppress("unchecked_cast")
    private fun <T : InstanceLoadingState> MutableStateFlow<InstanceLoadingState>.updateAs(
        function: (T) -> InstanceLoadingState
    ) = update { function(it as T) }
}
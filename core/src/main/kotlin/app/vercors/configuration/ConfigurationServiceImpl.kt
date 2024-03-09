package app.vercors.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private val logger = KotlinLogging.logger { }

class ConfigurationServiceImpl(
    coroutineScope: CoroutineScope,
    private val repository: ConfigurationRepository
) : ConfigurationService, CoroutineScope by coroutineScope {
    private val _loadingState: MutableStateFlow<ConfigurationLoadingState> =
        MutableStateFlow(ConfigurationLoadingState.NotLoaded)
    override val loadingState: StateFlow<ConfigurationLoadingState> = _loadingState
    override val configState: StateFlow<ConfigurationData?> = loadingState
        .map { if (it is ConfigurationLoadingState.Loaded) it.config else null }
        .stateIn(this, SharingStarted.Eagerly, null)
    override val config: ConfigurationData get() = (loadingState.value as ConfigurationLoadingState.Loaded).config

    init {
        launch { configState.filterNotNull().drop(1).collect { save() } }
    }

    override fun load(): Job = launch {
        logger.info { "Loading configuration" }
        val result = runCatching { repository.load() }
        val newState = result.fold(
            onSuccess = {
                logger.info { "Configuration loaded" }
                ConfigurationLoadingState.Loaded(it)
            },
            onFailure = {
                logger.error(it) { "An error occured while loading configuration" }
                ConfigurationLoadingState.Errored(it)
            }
        )
        _loadingState.update { newState }
    }

    override fun update(config: ConfigurationData, forceSave: Boolean) {
        _loadingState.update { ConfigurationLoadingState.Loaded(config) }
        if (forceSave) save()
    }

    private fun save(): Job = launch {
        logger.info { "Saving configuration" }
        configState.value?.let {
            repository.save(it)
        }
        logger.info { "Configuration saved" }
    }
}
package app.vercors.configuration

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.common.totalMemory
import app.vercors.home.HomeSectionType
import app.vercors.project.ProjectProviderType
import com.arkivanov.essenty.lifecycle.doOnStop
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import oshi.SystemInfo
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.absolutePathString
import kotlin.math.roundToInt

private val logger = KotlinLogging.logger { }

class ConfigurationComponentImpl(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.inject(),
    private val systemInfo: SystemInfo = componentContext.inject()
) : AbstractAppComponent(componentContext), ConfigurationComponent {
    private val _uiState = MutableStateFlow(
        ConfigurationUiState(
            hasCustomMemory = configurationService.config.defaultAllocatedRam != null,
            currentMemory = configurationService.config.defaultAllocatedRam ?: 1024
        )
    )
    override val uiState: StateFlow<ConfigurationUiState> = _uiState

    init {
        configurationService.configState.filterNotNull()
            .map { it.java8Path }.distinctUntilChanged()
            .collectInLifecycle {
                _uiState.update { it.copy(java8Status = ConfigurationJavaStatus.Checking) }
                launch {
                    val status = checkJavaPath(it, 8)
                    _uiState.update { it.copy(java8Status = status) }
                }
            }
        configurationService.configState.filterNotNull()
            .map { it.java17Path }.distinctUntilChanged()
            .collectInLifecycle {
                _uiState.update { it.copy(java17Status = ConfigurationJavaStatus.Checking) }
                launch {
                    val status = checkJavaPath(it, 17)
                    _uiState.update { it.copy(java17Status = status) }
                }
            }
        launchInLifecycle {
            val totalMemory = systemInfo.totalMemory()
            _uiState.update { it.copy(totalMemory = totalMemory) }
            while (true) {
                delay(1000)
                batchSaveConfig()
                ensureActive()
            }
        }
        doOnStop { batchSaveConfig() }
    }

    override fun onConfigChange(config: ConfigurationData) {
        configurationService.update(config)
    }

    override fun onHomeSectionChanged(section: HomeSectionType, configuration: ConfigurationData) {
        val sections =
            if (section in configuration.homeSections) configuration.homeSections - section
            else configuration.homeSections + section
        if (sections.isNotEmpty()) onConfigChange(configuration.copy(homeSections = sections.sorted()))
    }

    override fun onHomeProviderChanged(provider: ProjectProviderType, configuration: ConfigurationData) {
        val providers =
            if (provider in configuration.homeProviders) configuration.homeProviders - provider
            else configuration.homeProviders + provider
        if (providers.isNotEmpty()) onConfigChange(configuration.copy(homeProviders = providers.sorted()))
    }

    override fun refresh() {
        configurationService.load()
    }

    override fun closeDirectoryPicker() {
        _uiState.update {
            it.copy(
                showDirectoryPicker = false,
                initialPath = null,
                onSelectPath = { this }
            )
        }
    }

    override fun openDirectoryPicker(
        initialPath: String?,
        onSelectPath: ConfigurationData.(String) -> ConfigurationData
    ) {
        _uiState.update {
            it.copy(
                showDirectoryPicker = true,
                initialPath = initialPath ?: "/",
                onSelectPath = onSelectPath
            )
        }
    }

    override fun toggleCustomMemory() {
        _uiState.update { it.copy(hasCustomMemory = !it.hasCustomMemory) }
    }

    override fun onCustomMemoryChange(value: Int, fromSlider: Boolean) {
        val currentMemory = if (fromSlider) (value / 512.0).roundToInt() * 512 else value
        _uiState.update { it.copy(currentMemory = currentMemory) }
    }

    private suspend fun checkJavaPath(
        path: String?,
        javaVersion: Int,
        context: CoroutineContext = Dispatchers.IO
    ): ConfigurationJavaStatus {
        if (path == null) return ConfigurationJavaStatus.Invalid
        return withContext(context) {
            try {
                val executablePath = Path.of(path, "bin", "java").absolutePathString()
                val process = ProcessBuilder(executablePath, "-version").start()
                val versionLine = process.errorReader().readLine()
                if (process.waitFor() != 0) return@withContext ConfigurationJavaStatus.Invalid
                val versionNumbers = versionLine.split("\"")[1].split(".")
                val javaVersionToCheck =
                    if (javaVersion == 8) versionNumbers[1].toInt()
                    else versionNumbers[0].toInt()
                if (javaVersion == javaVersionToCheck) ConfigurationJavaStatus.Valid
                else ConfigurationJavaStatus.Invalid
            } catch (e: Throwable) {
                logger.warn(e) { "An error occured while checking Java $javaVersion version at location $path" }
                ConfigurationJavaStatus.Invalid
            }
        }
    }

    private fun batchSaveConfig() {
        val defaultAllocatedRam = if (uiState.value.hasCustomMemory) uiState.value.currentMemory else null
        onConfigChange(configurationService.config.copy(defaultAllocatedRam = defaultAllocatedRam))
    }
}
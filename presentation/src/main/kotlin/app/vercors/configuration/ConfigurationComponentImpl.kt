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

internal class ConfigurationComponentImpl(
    componentContext: AppComponentContext,
    private val configurationRepository: ConfigurationRepository = componentContext.inject(),
    private val systemInfo: SystemInfo = componentContext.inject()
) : AbstractAppComponent(componentContext, KotlinLogging.logger {}), ConfigurationComponent {
    private val _state = MutableStateFlow(
        ConfigurationState(
            hasCustomMemory = configurationRepository.current.defaultAllocatedRam != null,
            currentMemory = configurationRepository.current.defaultAllocatedRam ?: 1024
        )
    )
    override val state: StateFlow<ConfigurationState> = _state

    init {
        configurationRepository.state.filterNotNull()
            .map { it.java8Path }.distinctUntilChanged()
            .collectInLifecycle {
                _state.update { it.copy(java8Status = ConfigurationJavaStatus.Checking) }
                localScope.launch {
                    val status = checkJavaPath(it, 8)
                    _state.update { it.copy(java8Status = status) }
                }
            }
        configurationRepository.state.filterNotNull()
            .map { it.java17Path }.distinctUntilChanged()
            .collectInLifecycle {
                _state.update { it.copy(java17Status = ConfigurationJavaStatus.Checking) }
                localScope.launch {
                    val status = checkJavaPath(it, 17)
                    _state.update { it.copy(java17Status = status) }
                }
            }
        launchInLifecycle {
            val totalMemory = systemInfo.totalMemory()
            _state.update { it.copy(totalMemory = totalMemory) }
            while (true) {
                delay(1000)
                batchSaveConfig()
                ensureActive()
            }
        }
        doOnStop { batchSaveConfig() }
    }

    override fun onIntent(intent: ConfigurationIntent) = when (intent) {
        is ConfigurationIntent.UpdateConfig -> onUpdateConfig(intent.update)
        is ConfigurationIntent.UpdateHomeSection -> onUpdateHomeSection(intent.section)
        is ConfigurationIntent.UpdateHomeProvider -> onUpdateHomeProvider(intent.provider)
        is ConfigurationIntent.OpenDirectoryPicker -> onOpenDirectoryPicker(intent.initialPath, intent.onSelectPath)
        ConfigurationIntent.CloseDirectoryPicker -> onCloseDirectoryPicker()
        ConfigurationIntent.ToggleCustomMemory -> onToggleCustomMemory()
        is ConfigurationIntent.UpdateCustomMemory -> onUpdateCustomMemory(intent.value, intent.fromSlider)
    }

    override fun refresh() {
        localScope.launch { configurationRepository.loadConfiguration() }
    }

    private fun onUpdateConfig(update: (Configuration) -> Configuration) {
        localScope.launch { configurationRepository.updateConfiguration(update) }
    }

    private fun onUpdateHomeSection(section: HomeSectionType) {
        onUpdateConfig {
            val sections = if (section in it.homeSections) it.homeSections - section else it.homeSections + section
            if (sections.isNotEmpty()) it.copy(homeSections = sections.sorted()) else it
        }
    }

    private fun onUpdateHomeProvider(provider: ProjectProviderType) {
        onUpdateConfig {
            val providers =
                if (provider in it.homeProviders) it.homeProviders - provider else it.homeProviders + provider
            if (providers.isNotEmpty()) it.copy(homeProviders = providers.sorted()) else it
        }
    }

    private fun onCloseDirectoryPicker() {
        _state.update {
            it.copy(
                showDirectoryPicker = false,
                initialPath = null,
                onSelectPath = { this }
            )
        }
    }

    private fun onOpenDirectoryPicker(
        initialPath: String?,
        onSelectPath: Configuration.(String) -> Configuration
    ) {
        _state.update {
            it.copy(
                showDirectoryPicker = true,
                initialPath = initialPath ?: "/",
                onSelectPath = onSelectPath
            )
        }
    }

    private fun onToggleCustomMemory() {
        _state.update { it.copy(hasCustomMemory = !it.hasCustomMemory) }
    }

    private fun onUpdateCustomMemory(value: Int, fromSlider: Boolean) {
        val currentMemory = if (fromSlider) (value / 512.0).roundToInt() * 512 else value
        _state.update { it.copy(currentMemory = currentMemory) }
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
            } catch (e: Exception) {
                logger.warn(e) { "An error occured while checking Java $javaVersion version at location $path" }
                ConfigurationJavaStatus.Invalid
            }
        }
    }

    private fun batchSaveConfig() {
        val defaultAllocatedRam = if (state.value.hasCustomMemory) state.value.currentMemory else null
        onUpdateConfig { it.copy(defaultAllocatedRam = defaultAllocatedRam) }
    }
}
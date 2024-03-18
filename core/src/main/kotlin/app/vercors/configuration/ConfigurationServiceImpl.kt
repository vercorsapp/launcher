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

    override fun update(forceSave: Boolean, update: (ConfigurationData) -> ConfigurationData) {
        _loadingState.update {
            val config = (it as ConfigurationLoadingState.Loaded).config
            ConfigurationLoadingState.Loaded(update(config))
        }
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
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

import app.vercors.common.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private val logger = KotlinLogging.logger {}

internal class ConfigurationRepositoryImpl(
    private val externalScope: CoroutineScope,
    private val dataSource: ConfigurationDataSource
) : ConfigurationRepository {
    private val _state: MutableStateFlow<Resource<Configuration>> = createLoadableStateFlow()
    override val loadingState: StateFlow<Resource<Configuration>> = _state
    override val state: StateFlow<Configuration?> = loadingState.toResultStateFlow(externalScope)
    override val current: Configuration get() = (loadingState.value as Resource.Loaded).result

    override suspend fun loadConfiguration() {
        externalScope.launch {
            try {
                logger.info { "Loading configuration" }
                val configuration = dataSource.loadConfiguration()
                _state.emitLoaded(configuration)
                logger.info { "Configuration loaded" }
            } catch (e: Exception) {
                logger.error(e) { "An error occurred while loading configuration" }
                _state.emitErrored(e)
            }
        }.join()
    }

    override suspend fun updateConfiguration(updater: (Configuration) -> Configuration) {
        externalScope.launch {
            val configuration = _state.updateLoadedAndGet(updater)
            if (configuration is Resource.Loaded) dataSource.saveConfiguration(configuration.result)
        }.join()
    }

    override suspend fun initializeConfiguration(configuration: Configuration) {
        externalScope.launch {
            _state.emitLoaded(configuration)
            dataSource.saveConfiguration(configuration)
        }.join()
    }
}
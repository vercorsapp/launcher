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

import app.vercors.common.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

private val logger = KotlinLogging.logger { }

internal class InstanceRepositoryImpl(
    private val externalScope: CoroutineScope,
    private val dataSource: InstanceDataSource
) : InstanceRepository {
    private val _state: MutableStateFlow<Resource<List<Instance>>> = createLoadableStateFlow()
    override val loadingState: StateFlow<Resource<List<Instance>>> = _state
    override val state: StateFlow<List<Instance>?> = loadingState.toResultStateFlow(externalScope)
    override val current: List<Instance> get() = state.value!!

    override fun loadInstances() = channelFlow {
        externalScope.launch {
            try {
                _state.emitLoading(0f, emptyList())
                dataSource.loadInstances().collect { (progress, result) ->
                    send(result)
                    if (result is InstanceResult.Success) {
                        _state.updateLoading(progress) { it!! + Instance(result.instance, InstanceStatus.Stopped) }
                    }
                }
                _state.toLoaded()
            } catch (e: Exception) {
                _state.emitErrored(e)
            }
        }.join()
    }

    override suspend fun updateInstanceData(id: String, updater: (InstanceData) -> InstanceData) {
        externalScope.launch {
            val instances = _state.updateLoadedAndGet { instances ->
                instances.map { if (it.data.id == id) it.copy(data = updater(it.data)) else it }
            }
            if (instances is Resource.Loaded) {
                instances.result.find { it.data.id == id }?.let { dataSource.saveInstance(it.data) }
            }
        }.join()
    }

    override fun updateInstanceStatus(id: String, updater: (InstanceStatus) -> InstanceStatus) {
        _state.updateLoaded { instances ->
            instances.map { if (it.data.id == id) it.copy(status = updater(it.status)) else it }
        }
    }

    override suspend fun createInstance(instance: InstanceData) {
        externalScope.launch {
            logger.info { "Creating instance ${instance.name}" }
            val savedInstance = dataSource.saveInstance(instance)
            _state.updateLoaded { it + Instance(savedInstance, InstanceStatus.Stopped) }
            logger.info { "Created instance ${savedInstance.name}" }
        }.join()
    }
}
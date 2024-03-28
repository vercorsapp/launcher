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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private val logger = KotlinLogging.logger {}

internal class InstanceRepositoryImpl(
    private val externalScope: CoroutineScope,
    private val dataSource: InstanceDataSource
) : InstanceRepository {
    private val _loadingState: MutableStateFlow<Resource<List<Instance>>> = createLoadableStateFlow()
    override val loadingState: StateFlow<Resource<List<Instance>>> = _loadingState
    override val state: StateFlow<List<Instance>?> = loadingState.toResultStateFlow(externalScope)
    override val current: List<Instance> get() = state.value!!

    override fun loadInstances() = flow {
        try {
            _loadingState.emitLoading(0f, emptyList())
            dataSource.loadInstances().collect { (progress, result) ->
                emit(result)
                if (result is InstanceResult.Success) {
                    _loadingState.updateLoading(progress) {
                        it!! + Instance(
                            result.instance,
                            InstanceStatus.NotRunning
                        )
                    }
                }
            }
            _loadingState.toLoaded()
        } catch (e: Exception) {
            _loadingState.emitErrored(e)
        }
    }

    override fun findInstance(id: String): Instance? = current.find { it.id == id }

    override suspend fun updateInstanceData(id: String, updater: (InstanceData) -> InstanceData) {
        externalScope.launch {
            val instances = _loadingState.updateLoadedAndGet { instances ->
                instances.map { if (it.id == id) it.copy(data = updater(it.data)) else it }
            }
            if (instances is Resource.Loaded) {
                instances.result.find { it.id == id }?.let { dataSource.saveInstance(it.data) }
            }
        }.join()
    }

    override fun updateInstanceStatus(id: String, updater: (InstanceStatus) -> InstanceStatus) {
        _loadingState.updateLoaded { instances ->
            instances.map { if (it.id == id) it.copy(status = updater(it.status)) else it }
        }
    }

    override suspend fun createInstance(instance: InstanceData): String {
        return externalScope.async {
            logger.info { "Creating instance ${instance.name}" }
            val savedInstance = dataSource.saveInstance(instance)
            _loadingState.updateLoaded { it + Instance(savedInstance, InstanceStatus.NotRunning) }
            logger.info { "Created instance ${savedInstance.name}" }
            savedInstance.id
        }.await()
    }
}
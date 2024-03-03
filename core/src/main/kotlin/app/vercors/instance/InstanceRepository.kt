package app.vercors.instance

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface InstanceRepository {
    fun loadInstances(
        maximumParallelThreads: Int,
        context: CoroutineContext = Dispatchers.IO
    ): Flow<InstanceLoadingResult>

    suspend fun saveInstance(instance: InstanceData, context: CoroutineContext = Dispatchers.IO): InstanceData
    suspend fun deleteInstance(instance: InstanceData, context: CoroutineContext = Dispatchers.IO)
}

package app.vercors.instance

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant

interface InstanceService {
    val loadingState: StateFlow<InstanceLoadingState>
    val instancesState: StateFlow<List<InstanceData>?>
    val instances: List<InstanceData>
    fun loadInstances(): Job
    fun createInstance(instance: InstanceData)
    fun updateInstance(id: String, update: (InstanceData) -> InstanceData)
    fun tickRunningInstance(id: String, lastInstant: Instant?)
    fun deleteInstance(id: String): Job
}

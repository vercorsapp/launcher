package app.vercors.instance

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface InstanceService {
    val loadingState: StateFlow<InstanceLoadingState>
    val instancesState: StateFlow<List<InstanceData>?>
    val instances: List<InstanceData>
    fun loadInstances(): Job
    fun createInstance(instance: InstanceData): Deferred<InstanceData>
    fun updateInstance(instance: InstanceData): Job
    fun deleteInstance(instance: InstanceData): Job
    fun updateInstanceStatus(instance: InstanceData, status: InstanceStatus)
}

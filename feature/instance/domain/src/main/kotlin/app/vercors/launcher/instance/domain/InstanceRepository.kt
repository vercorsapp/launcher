package app.vercors.launcher.instance.domain

import kotlinx.coroutines.flow.Flow

interface InstanceRepository {
    fun observeAll(): Flow<List<Instance>>
    fun observeById(id: Int): Flow<Instance>
    suspend fun upsert(instance: Instance)
    suspend fun delete(instance: Instance)
}
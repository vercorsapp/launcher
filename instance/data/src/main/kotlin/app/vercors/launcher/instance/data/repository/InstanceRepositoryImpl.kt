package app.vercors.launcher.instance.data.repository

import app.vercors.launcher.instance.data.dao.InstanceDao
import app.vercors.launcher.instance.data.mapper.InstanceEntityMapper
import app.vercors.launcher.instance.domain.model.Instance
import app.vercors.launcher.instance.domain.repository.InstanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class InstanceRepositoryImpl(
    private val dao: InstanceDao,
    private val mapper: InstanceEntityMapper
) : InstanceRepository {
    override fun observeAll(): Flow<List<Instance>> = dao.observeInstances()
        .map { instances -> instances.map { mapper.fromEntity(it) } }
        .distinctUntilChanged()

    override fun observeById(id: Int): Flow<Instance> = dao.observeInstanceById(id)
        .map { mapper.fromEntity(it) }
        .distinctUntilChanged()

    override suspend fun upsert(instance: Instance) = dao.upsert(mapper.toEntity(instance))

    override suspend fun delete(instance: Instance) = dao.delete(mapper.toEntity(instance))
}
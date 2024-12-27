package app.vercors.launcher.instance.data

import app.vercors.launcher.instance.domain.Instance
import app.vercors.launcher.instance.domain.InstanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class InstanceRepositoryImpl(
    private val dao: InstanceDao
) : InstanceRepository {
    override fun observeAll(): Flow<List<Instance>> = dao.observeInstances()
        .map { instances -> instances.map { it.toInstance() } }
        .distinctUntilChanged()

    override fun observeById(id: Int): Flow<Instance> = dao.observeInstanceById(id)
        .map { it.toInstance() }
        .distinctUntilChanged()

    override suspend fun create(instance: Instance) = dao.insert(instance.toEntity())

    override suspend fun delete(instance: Instance) = dao.delete(instance.toEntity())
}
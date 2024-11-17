package app.vercors.launcher.instance.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface InstanceDao {
    @Query("SELECT * FROM instance")
    fun observeInstances(): Flow<List<InstanceEntity>>

    @Query("SELECT * FROM instance WHERE id = :id")
    fun observeInstanceById(id: Int): Flow<InstanceEntity>

    @Upsert
    suspend fun upsert(instance: InstanceEntity)

    @Delete
    suspend fun delete(instance: InstanceEntity)
}
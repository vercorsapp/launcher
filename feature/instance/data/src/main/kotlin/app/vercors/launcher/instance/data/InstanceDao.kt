package app.vercors.launcher.instance.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InstanceDao {
    @Query("SELECT * FROM instance")
    fun observeInstances(): Flow<List<InstanceEntity>>

    @Query("SELECT * FROM instance WHERE id = :id")
    fun observeInstanceById(id: Int): Flow<InstanceEntity>

    @Insert
    suspend fun insert(instance: InstanceEntity)

    @Upsert
    suspend fun upsert(instance: InstanceEntity)

    @Update
    suspend fun update(instance: InstanceEntity)

    @Delete
    suspend fun delete(instance: InstanceEntity)
}
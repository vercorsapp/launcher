package app.vercors.launcher.instance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Entity(tableName = "instance")
data class InstanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val gameVersion: String,
    val modLoader: String?,
    val modLoaderVersion: String?,
    val createdAt: Instant,
    val lastPlayedAt: Instant,
    val playTime: Duration
)

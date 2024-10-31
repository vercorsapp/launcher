package app.vercors.launcher.instance.domain.model

import kotlinx.datetime.Instant
import kotlin.time.Duration

typealias InstanceId = Long

data class Instance(
    val id: InstanceId,
    val name: String,
    val gameVersion: String,
    val modLoader: InstanceModLoader?,
    val createdAt: Instant,
    val lastPlayedAt: Instant,
    val playTime: Duration
)

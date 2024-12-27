package app.vercors.launcher.instance.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

typealias InstanceId = Long

data class Instance(
    val id: InstanceId = 0,
    val name: String,
    val gameVersion: String,
    val modLoader: InstanceModLoader? = null,
    val createdAt: Instant = Clock.System.now(),
    val lastPlayedAt: Instant? = null,
    val playTime: Duration = Duration.ZERO
)
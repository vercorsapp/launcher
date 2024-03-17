package app.vercors.instance

import app.vercors.common.AppDuration
import app.vercors.common.AppInstant
import app.vercors.instance.mojang.data.MojangVersionManifest
import app.vercors.project.ModLoader
import kotlinx.coroutines.Job
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.Duration
import java.time.Instant

@Serializable
data class InstanceData(
    val version: Int = 0,
    val name: String,
    val id: String = "",
    val icon: Icon? = null,
    val gameVersion: MojangVersionManifest.Version,
    val loader: ModLoader? = null,
    val loaderVersion: String? = null,
    val dateCreated: AppInstant = Instant.now(),
    val dateModified: AppInstant = Instant.now(),
    val lastPlayed: AppInstant? = null,
    val timePlayed: AppDuration = Duration.ZERO,
    @Transient
    var dirty: Boolean = false,
    @Transient
    val status: InstanceStatus = InstanceStatus.Stopped,
    @Transient
    val preparationJob: Job? = null,
    @Transient
    val runJob: Job? = null
) {

    @Serializable
    data class Icon(
        val type: Type,
        val name: String
    ) {
        @Serializable
        enum class Type {
            @SerialName("base")
            Base,

            @SerialName("custom")
            Custom
        }
    }
}

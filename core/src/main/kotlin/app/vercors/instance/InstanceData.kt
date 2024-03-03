package app.vercors.instance

import app.vercors.common.AppDuration
import app.vercors.common.AppInstant
import app.vercors.instance.mojang.data.MojangVersionManifest
import app.vercors.project.ModLoader
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.Instant

@Serializable
data class InstanceData(
    val name: String,
    val path: String = "",
    val icon: Icon? = null,
    val gameVersion: MojangVersionManifest.Version,
    val loader: ModLoader? = null,
    val loaderVersion: String? = null,
    val dateCreated: AppInstant = Instant.now(),
    val dateModified: AppInstant = Instant.now(),
    val lastPlayed: AppInstant? = null,
    val timePlayed: AppDuration = Duration.ZERO
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

package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Instance(
    val name: String,
    val path: String = "",
    val icon: Icon? = null,
    val gameVersion: MojangVersionManifest.Version,
    val loader: Loader? = null,
    val loaderVersion: String? = null,
    val dateCreated: AppInstant = Instant.now(),
    val dateModified: AppInstant = Instant.now(),
    val lastPlayed: AppInstant? = null
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

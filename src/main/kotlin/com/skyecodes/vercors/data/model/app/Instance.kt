package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Instance(
    val name: String,
    var path: String = "",
    val gameVersion: MojangVersionManifest.Version,
    val loader: Loader? = null,
    val loaderVersion: String? = null,
    val created: AppInstant = Instant.now(),
    val lastLaunched: AppInstant? = null
)

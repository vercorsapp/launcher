package com.skyecodes.vercors.data.model.mojang

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MojangReleaseType {
    @SerialName("release")
    Release,

    @SerialName("snapshot")
    Snapshot,

    @SerialName("old_beta")
    Beta,

    @SerialName("old_alpha")
    Alpha,
}

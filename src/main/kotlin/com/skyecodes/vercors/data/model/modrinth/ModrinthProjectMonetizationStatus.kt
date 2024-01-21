package com.skyecodes.vercors.data.model.modrinth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModrinthProjectMonetizationStatus {
    @SerialName("monetized")
    Monetized,

    @SerialName("demonetized")
    Demonetized,

    @SerialName("force-demonetized")
    ForceDemonetized
}
package com.skyecodes.vercors.projects.modrinth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModrinthProjectSideSupport {
    @SerialName("required")
    Required,

    @SerialName("optional")
    Optional,

    @SerialName("unsupported")
    Unsupported,

    @SerialName("unknown")
    Unknown
}
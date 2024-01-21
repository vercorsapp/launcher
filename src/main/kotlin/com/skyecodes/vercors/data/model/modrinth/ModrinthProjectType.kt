package com.skyecodes.vercors.data.model.modrinth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModrinthProjectType(val value: String) {
    @SerialName("mod")
    Mod("mod"),

    @SerialName("modpack")
    Modpack("modpack"),

    @SerialName("resourcepack")
    ResourcePack("resourcepack"),

    @SerialName("shader")
    ShaderPack("shader")
}

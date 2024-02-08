package com.skyecodes.vercors.projects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Provider(val value: String, val text: String) {
    @SerialName("modrinth")
    Modrinth("modrinth", "Modrinth"),

    @SerialName("curseforge")
    Curseforge("curseforge", "Curseforge")
}

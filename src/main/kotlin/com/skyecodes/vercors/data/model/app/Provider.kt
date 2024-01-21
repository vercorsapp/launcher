package com.skyecodes.vercors.data.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Provider(val value: String) {
    @SerialName("modrinth")
    Modrinth("modrinth"),

    @SerialName("curseforge")
    Curseforge("curseforge")
}

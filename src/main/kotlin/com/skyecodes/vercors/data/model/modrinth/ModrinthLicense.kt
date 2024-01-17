package com.skyecodes.vercors.data.model.modrinth


import kotlinx.serialization.Serializable

@Serializable
data class ModrinthLicense(
    val id: String,
    val name: String,
    val url: String
)
package com.skyecodes.vercors.data.model.modrinth


import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProjectDonationUrl(
    val id: String,
    val platform: String,
    val url: String
)
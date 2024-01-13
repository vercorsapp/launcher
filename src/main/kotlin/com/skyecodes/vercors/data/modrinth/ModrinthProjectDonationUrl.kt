package com.skyecodes.vercors.data.modrinth


import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProjectDonationUrl(
    val id: String,
    val platform: String,
    val url: String
)
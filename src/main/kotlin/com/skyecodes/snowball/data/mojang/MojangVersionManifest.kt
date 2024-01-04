package com.skyecodes.snowball.data.mojang


import kotlinx.serialization.Serializable

@Serializable
data class VersionManifest(
    val latest: Latest,
    val versions: List<Version>
) {
    @Serializable
    data class Latest(
        val release: String,
        val snapshot: String
    )

    @Serializable
    data class Version(
        val complianceLevel: Int,
        val id: String,
        val releaseTime: MojangInstant,
        val sha1: String,
        val time: MojangInstant,
        val type: String,
        val url: String
    )
}

operator fun List<VersionManifest.Version>.get(id: String): VersionManifest.Version = first { it.id == id }
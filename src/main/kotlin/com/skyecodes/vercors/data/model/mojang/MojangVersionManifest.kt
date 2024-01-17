package com.skyecodes.vercors.data.model.mojang


import kotlinx.serialization.Serializable

@Serializable
data class MojangVersionManifest(
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
        val type: MojangReleaseType,
        val url: String
    )

    val latestRelease get() = versions[latest.release]
    val latestSnapshot get() = versions[latest.release]

    fun isLatestRelease(version: Version) = version.id == latest.release
    fun isLatestSnapshot(version: Version) = version.id == latest.snapshot
}

operator fun List<MojangVersionManifest.Version>.get(id: String): MojangVersionManifest.Version = first { it.id == id }
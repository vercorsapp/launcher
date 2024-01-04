package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.mojang.MojangVersionInfo
import com.skyecodes.snowball.data.mojang.VersionManifest
import io.ktor.client.call.*
import io.ktor.client.request.*

object MojangApi {
    private const val VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"

    suspend fun getVersionManifest(): VersionManifest = HttpClient.get(VERSION_MANIFEST_URL).body()

    suspend fun getVersionInfo(url: String): MojangVersionInfo = HttpClient.get(url).body()
}
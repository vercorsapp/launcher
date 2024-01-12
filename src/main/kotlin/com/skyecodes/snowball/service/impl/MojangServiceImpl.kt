package com.skyecodes.snowball.service.impl

import com.skyecodes.snowball.data.mojang.MojangVersionInfo
import com.skyecodes.snowball.data.mojang.MojangVersionManifest
import com.skyecodes.snowball.service.HttpService
import com.skyecodes.snowball.service.MojangService
import io.ktor.client.call.*
import io.ktor.client.request.*

class MojangServiceImpl(private val httpService: HttpService) : MojangService {
    override suspend fun getVersionManifest(): MojangVersionManifest =
        httpService.client.get(VERSION_MANIFEST_URL).body()

    override suspend fun getVersionInfo(url: String): MojangVersionInfo = httpService.client.get(url).body()

    companion object {
        private const val VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"
    }
}
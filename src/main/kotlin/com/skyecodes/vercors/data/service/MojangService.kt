package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.data.model.mojang.MojangVersionInfo
import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import io.ktor.client.call.*
import io.ktor.client.request.*

interface MojangService {
    suspend fun getVersionManifest(): MojangVersionManifest

    suspend fun getVersionInfo(url: String): MojangVersionInfo
}

class MojangServiceImpl(private val httpService: HttpService) : MojangService {
    override suspend fun getVersionManifest(): MojangVersionManifest =
        httpService.client.get(VERSION_MANIFEST_URL).body()

    override suspend fun getVersionInfo(url: String): MojangVersionInfo = httpService.client.get(url).body()

    companion object {
        private const val VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"
    }
}
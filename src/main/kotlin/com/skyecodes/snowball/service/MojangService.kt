package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.mojang.MojangVersionInfo
import com.skyecodes.snowball.data.mojang.VersionManifest

interface MojangService {
    suspend fun getVersionManifest(): VersionManifest

    suspend fun getVersionInfo(url: String): MojangVersionInfo
}
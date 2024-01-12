package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.mojang.MojangVersionInfo
import com.skyecodes.snowball.data.mojang.MojangVersionManifest

interface MojangService {
    suspend fun getVersionManifest(): MojangVersionManifest

    suspend fun getVersionInfo(url: String): MojangVersionInfo
}
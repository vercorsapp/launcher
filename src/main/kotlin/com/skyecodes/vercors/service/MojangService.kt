package com.skyecodes.vercors.service

import com.skyecodes.vercors.data.mojang.MojangVersionInfo
import com.skyecodes.vercors.data.mojang.MojangVersionManifest

interface MojangService {
    suspend fun getVersionManifest(): MojangVersionManifest

    suspend fun getVersionInfo(url: String): MojangVersionInfo
}
package app.vercors.instance.mojang

import app.vercors.instance.mojang.data.MojangVersionManifest

interface MojangService {
    suspend fun getVersionManifest(): MojangVersionManifest
    fun getAssetUrl(sha1: String, name: String): String
}

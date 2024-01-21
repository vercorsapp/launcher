package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.data.model.mojang.MojangVersionInfo
import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

interface MojangService {
    suspend fun getVersionManifest(): MojangVersionManifest
    suspend fun getVersionInfo(url: String): MojangVersionInfo
    fun clearCache()
}

class MojangServiceImpl(
    coroutineScope: CoroutineScope,
    private val httpClient: HttpClient,
    private val manifestVersionUrl: String = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"
) : MojangService, CoroutineScope by coroutineScope {
    private var manifest: Deferred<MojangVersionManifest> = getDeferredManifest()
    private var deferredVersionInfo: MutableMap<String, Deferred<MojangVersionInfo>> = mutableMapOf()

    override suspend fun getVersionManifest(): MojangVersionManifest =
        manifest.await()

    private fun getDeferredManifest(): Deferred<MojangVersionManifest> =
        async(start = CoroutineStart.LAZY) { httpClient.get(manifestVersionUrl).body() }

    override suspend fun getVersionInfo(url: String): MojangVersionInfo =
        deferredVersionInfo.getOrPut(url) { getDeferredVersionInfo(url) }.await()

    private fun getDeferredVersionInfo(url: String): Deferred<MojangVersionInfo> =
        async { httpClient.get(url).body() }

    override fun clearCache() {
        manifest = getDeferredManifest()
        deferredVersionInfo = deferredVersionInfo.filterValues { it.isActive }.toMutableMap()
    }
}

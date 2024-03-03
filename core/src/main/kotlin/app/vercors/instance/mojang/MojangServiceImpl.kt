package app.vercors.instance.mojang

import app.vercors.instance.mojang.data.MojangVersionManifest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class MojangServiceImpl(
    coroutineScope: CoroutineScope,
    private val httpClient: HttpClient,
    private val manifestVersionUrl: String = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json",
    private val assetUrlRoot: String = "https://resources.download.minecraft.net"
) : MojangService, CoroutineScope by coroutineScope {
    private var manifest: Deferred<MojangVersionManifest> = getDeferredManifest()

    override suspend fun getVersionManifest(): MojangVersionManifest =
        manifest.await()

    private fun getDeferredManifest(): Deferred<MojangVersionManifest> =
        async(start = CoroutineStart.LAZY) { httpClient.get(manifestVersionUrl).body() }

    override fun getAssetUrl(sha1: String, name: String): String = "$assetUrlRoot/${sha1.take(2)}/$sha1"

    override fun clearCache() {
        manifest = getDeferredManifest()
    }
}
package app.vercors.instance.mojang

import app.vercors.instance.mojang.data.MojangVersionManifest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope

class MojangServiceImpl(
    coroutineScope: CoroutineScope,
    private val httpClient: HttpClient,
    private val manifestVersionUrl: String = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json",
    private val assetUrlRoot: String = "https://resources.download.minecraft.net"
) : MojangService, CoroutineScope by coroutineScope {
    override suspend fun getVersionManifest(): MojangVersionManifest = httpClient.get(manifestVersionUrl).body()

    override fun getAssetUrl(sha1: String, name: String): String = "$assetUrlRoot/${sha1.take(2)}/$sha1"
}
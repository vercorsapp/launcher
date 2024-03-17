/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
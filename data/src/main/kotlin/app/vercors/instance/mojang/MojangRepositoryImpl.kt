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

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

class MojangRepositoryImpl(
    private val mojangRemoteDataSource: MojangRemoteDataSource,
    private val mojangLocalDataSource: MojangLocalDataSource
) : MojangRepository {
    private val versionManifestMutex = Mutex()
    private var versionManifestCache: MojangVersionManifest? = null
    private val versionInfoCache = ConcurrentHashMap<String, MojangVersionInfo>()
    private val assetIndexCache = ConcurrentHashMap<String, MojangAssetIndex>()

    override suspend fun getVersionManifest(): MojangVersionManifest {
        if (versionManifestCache == null) {
            val result = mojangRemoteDataSource.getVersionManifest()
            versionManifestMutex.withLock { versionManifestCache = result }
        }
        return versionManifestMutex.withLock { versionManifestCache!! }
    }

    override suspend fun getVersionInfo(version: MojangVersionManifest.Version): MojangVersionInfo =
        versionInfoCache.getOrPut(version.id) {
            mojangLocalDataSource.getVersionInfo(version.id) ?: mojangRemoteDataSource
                .getVersionInfo(version).also { mojangLocalDataSource.saveVersionInfo(version.id, it) }
        }

    override suspend fun getAssetIndex(version: MojangVersionInfo): MojangAssetIndex =
        assetIndexCache.getOrPut(version.id) {
            mojangLocalDataSource.getAssetIndex(version.assets) ?: mojangRemoteDataSource
                .getAssetIndex(version.assetIndex).also { mojangLocalDataSource.saveAssetIndex(version.assets, it) }
        }

    override fun getAssetUrl(sha1: String, name: String): String = mojangRemoteDataSource.getAssetUrl(sha1, name)
}
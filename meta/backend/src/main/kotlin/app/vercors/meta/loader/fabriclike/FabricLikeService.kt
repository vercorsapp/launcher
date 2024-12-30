/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.meta.loader.fabriclike

import app.vercors.meta.loader.LoaderServiceBase
import app.vercors.meta.loader.MetaLoaderVersionList
import app.vercors.meta.loader.loaderCacheDuration
import app.vercors.meta.loader.metaLoaderVersionList
import app.vercors.meta.project.MetaProjectInstaller
import app.vercors.meta.project.metaProjectInstaller
import app.vercors.meta.utils.InMemoryCache
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope

private val logger = KotlinLogging.logger {}

abstract class FabricLikeService(
    private val name: String,
    private val api: FabricLikeApi,
    private val coroutineScope: CoroutineScope,
    private val installerArgs: List<String>
) : InMemoryCache<FabricLikeData>(coroutineScope, loaderCacheDuration), LoaderServiceBase {
    override suspend fun getLoaderVersionsForGameVersion(gameVersion: String): MetaLoaderVersionList? {
        val data = getData()
        return if (gameVersion in data.minecraftVersions) data.loaderVersions else null
    }

    override suspend fun getInstaller(): MetaProjectInstaller? = getData().installerVersion

    override suspend fun fetchData(): FabricLikeData {
        logger.info { "Loading $name Loader data..." }
        val (game, loader, installer) = api.getAllVersions()
        val minecraftVersions = game.map { it.version }
        val latestInstaller = installer.first()
        val installerVersion = metaProjectInstaller {
            url = latestInstaller.url
            maven = latestInstaller.maven
            version = latestInstaller.version
            minimumJavaVersion = 8
            arguments.addAll(installerArgs)
        }
        val loaderVersions = metaLoaderVersionList {
            loader.firstOrNull { it.stable ?: !it.version.contains('-') }?.let { this.recommended = it.version }
            loader.firstOrNull()?.let { this.latest = it.version }
            this.versions += loader.map { it.version }
        }
        logger.info { "Loaded $name Loader data" }
        return FabricLikeData(minecraftVersions, loaderVersions, installerVersion)
    }
}

data class FabricLikeData(
    val minecraftVersions: List<String>,
    val loaderVersions: MetaLoaderVersionList,
    val installerVersion: MetaProjectInstaller,
)
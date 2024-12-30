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

package app.vercors.meta.loader.forge

import app.vercors.meta.loader.MetaLoaderVersionList
import app.vercors.meta.loader.loaderCacheDuration
import app.vercors.meta.loader.metaLoaderVersionList
import app.vercors.meta.project.MetaProjectInstaller
import app.vercors.meta.utils.InMemoryCache
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {}

@Single
class ForgeServiceImpl(
    private val forgeApi: ForgeApi,
    coroutineScope: CoroutineScope
) : InMemoryCache<VersionMap>(coroutineScope, loaderCacheDuration), ForgeService {
    override suspend fun getLoaderVersionsForGameVersion(gameVersion: String): MetaLoaderVersionList? {
        return getData()[gameVersion]
    }

    override suspend fun getInstaller(): MetaProjectInstaller? {
        TODO("Not yet implemented")
    }

    override suspend fun fetchData(): VersionMap = coroutineScope {
        logger.info { "Loading Forge Loader data..." }
        val promotions = async { forgeApi.getPromotions().promos }
        val metadata = forgeApi.getMavenMetadata()
        val data = metadata.versioning.versions
            .map { it.split('-') }
            .filter { it.size == 2 }
            .groupBy({ it[0] }) { it[1] }
            .mapValues { (mcVersion, forgeVersions) ->
                metaLoaderVersionList {
                    latest = "$mcVersion-${forgeVersions.first()}"
                    promotions.await()["$mcVersion-recommended"]?.let { recommended = "$mcVersion-$it" }
                    versions.addAll(forgeVersions.map { "$mcVersion-$it" })
                }
            }
        logger.info { "Loaded Forge Loader data" }
        data
    }
}

private typealias VersionMap = Map<String, MetaLoaderVersionList>
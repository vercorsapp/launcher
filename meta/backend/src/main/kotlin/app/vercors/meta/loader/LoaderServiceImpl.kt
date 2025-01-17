/*
 * Copyright (c) 2024-2025 skyecodes
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

package app.vercors.meta.loader

import app.vercors.meta.loader.fabric.FabricService
import app.vercors.meta.loader.forge.ForgeService
import app.vercors.meta.loader.neoforge.NeoForgeService
import app.vercors.meta.loader.quilt.QuiltService
import org.koin.core.annotation.Single

@Single
class LoaderServiceImpl(
    private val fabricService: FabricService,
    private val forgeService: ForgeService,
    private val neoforgeService: NeoForgeService,
    private val quiltService: QuiltService
) : LoaderService {
    override suspend fun getLoaderVersionsForGameVersion(
        loaderType: MetaLoaderType,
        gameVersion: String
    ): MetaLoaderVersionList? =
        getLoader(loaderType)?.getLoaderVersionsForGameVersion(gameVersion)

    override suspend fun getAllLoadersVersionsForGameVersion(gameVersion: String): MetaLoaderVersionMap =
        MetaLoaderType.entries.filter { it != MetaLoaderType.UNRECOGNIZED }
            .associate { it.name to getLoaderVersionsForGameVersion(it, gameVersion) }
            .filterValues { it != null }
            .mapValues { it.value!! }
            .let {
                metaLoaderVersionMap {
                    loaderMap.putAll(it)
                }
            }

    private fun getLoader(loaderType: MetaLoaderType): LoaderServiceBase? = when (loaderType) {
        MetaLoaderType.fabric -> fabricService
        MetaLoaderType.forge -> forgeService
        MetaLoaderType.neoforge -> neoforgeService
        MetaLoaderType.quilt -> quiltService
        else -> null
    }
}

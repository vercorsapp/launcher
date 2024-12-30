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

package app.vercors.meta.home

import app.vercors.meta.home.HomeServiceImpl.HomeKey
import app.vercors.meta.mapAsync
import app.vercors.meta.project.MetaProjectProvider
import app.vercors.meta.project.MetaProjectType
import app.vercors.meta.project.ProjectService
import app.vercors.meta.utils.InMemoryCache
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {}

@Single
class HomeServiceImpl(
    coroutineScope: CoroutineScope,
    private val projectService: ProjectService
) : InMemoryCache<HomeData>(coroutineScope, homeCacheDuration), HomeService {
    override suspend fun getHomeProjects(
        provider: MetaProjectProvider,
        types: List<MetaProjectType>
    ): MetaHomeSectionList {
        val data = getData()
            .filterKeys { (keyProvider, keyType) -> keyProvider == provider && keyType in types }
            .map { it.value }
        return metaHomeSectionList { sections.addAll(data) }
    }

    override suspend fun fetchData(): Map<HomeKey, MetaHomeSection> {
        logger.info { "Loading home data" }
        val data = MetaProjectProvider.entries
            .filter { it != MetaProjectProvider.UNRECOGNIZED }
            .flatMap { provider ->
                MetaProjectType.entries
                    .filter { it != MetaProjectType.UNRECOGNIZED }
                    .map { type -> HomeKey(provider, type) }
            }
            .mapAsync {
                it to metaHomeSection {
                    type = it.type
                    projects.addAll(
                        projectService.searchProject(
                            provider = it.provider,
                            type = it.type,
                            limit = 10
                        )
                    )
                }
            }
            .toMap()
        logger.info { "Loaded home data" }
        return data
    }

    data class HomeKey(val provider: MetaProjectProvider, val type: MetaProjectType)
}

typealias HomeData = Map<HomeKey, MetaHomeSection>
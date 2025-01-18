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

package app.vercors.launcher.project.data.remote.modrinth

import app.vercors.launcher.project.data.remote.ProviderDataSource
import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectType
import app.vercors.lib.domain.DomainError
import app.vercors.lib.domain.Resource
import app.vercors.lib.domain.map
import app.vercors.lib.domain.observeResource
import app.vercors.lib.platform.modrinth.ModrinthApi
import io.ktor.client.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class ModrinthDataSource(private val api: ModrinthApi) : ProviderDataSource {
    override fun findProjects(
        projectType: ProjectType,
        query: String?,
        limit: Int
    ): Flow<Resource<List<Project>, DomainError>> =
        observeResource {
            api.search(
                query = query,
                facets = buildFacets(projectType),
                limit = limit
            ).map { result -> result.hits.map { it.toProject() } }
        }

    private fun buildFacets(projectType: ProjectType): String =
        "[[\"project_type:${projectType.toModrinthProjectType().value}\"]]"
}

@Single
fun provideModrinthApi(
    httpClient: HttpClient
): ModrinthApi = ModrinthApi(httpClient)

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

package app.vercors.launcher.project.data.remote.curseforge

import app.vercors.launcher.project.data.remote.ProviderDataSource
import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectType
import app.vercors.lib.domain.DomainError
import app.vercors.lib.domain.Resource
import app.vercors.lib.domain.map
import app.vercors.lib.domain.observeResource
import app.vercors.lib.platform.curseforge.CurseForgeApi
import io.ktor.client.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single

@Single
class CurseForgeDataSource(private val api: CurseForgeApi) : ProviderDataSource {
    override fun findProjects(
        projectType: ProjectType,
        query: String?,
        limit: Int
    ): Flow<Resource<List<Project>, DomainError>> =
        observeResource {
            api.search(
                gameId = MINECRAFT_GAME_ID,
                classId = projectType.toClassId(),
                pageSize = limit
            ).map { result -> result.data.map { it.toProject() } }
        }

    companion object {
        private const val MINECRAFT_GAME_ID = 432
    }
}

@Single
fun provideCurseForgeApi(
    httpClient: HttpClient,
    @Property("curseForgeApiKey") curseForgeApiKey: String,
): CurseForgeApi = CurseForgeApi(httpClient, curseForgeApiKey)

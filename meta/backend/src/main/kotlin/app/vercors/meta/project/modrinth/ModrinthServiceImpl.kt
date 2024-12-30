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

package app.vercors.meta.project.modrinth

import app.vercors.meta.project.MetaProject
import app.vercors.meta.project.MetaProjectType
import app.vercors.meta.project.metaProject
import org.koin.core.annotation.Single

@Single
class ModrinthServiceImpl(private val api: ModrinthApi) : ModrinthService {
    override suspend fun search(type: MetaProjectType, limit: Int) = api.search(
        facets = listOf(mapOf(type.asFacet())).asString(),
        limit = limit
    ).hits.convertAll()

    private fun List<ModrinthProjectResult>.convertAll(): List<MetaProject> = map { convert(it) }

    private fun convert(project: ModrinthProjectResult): MetaProject = metaProject {
        name = project.title
        author = project.author
        project.iconUrl?.let { logoUrl = it }
        project.featuredGallery?.let { imageUrl = it }
        description = project.description
    }

    private val projectTypeToFacet = mapOf(
        MetaProjectType.mod to "mod",
        MetaProjectType.modpack to "modpack",
        MetaProjectType.resourcepack to "resourcepack",
        MetaProjectType.shader to "shader",
    )

    private fun MetaProjectType.asFacet(): Pair<String, String> = "project_type" to projectTypeToFacet.getValue(this)

    private fun List<Map<String, String>>.asString(): String = joinToString(",") { map ->
        map.entries.joinToString(",") { (key, value) -> "\"$key:$value\"" }.let { "[$it]" }
    }.let { "[$it]" }
}

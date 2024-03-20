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

package app.vercors.project.modrinth

import app.vercors.project.Project
import app.vercors.project.ProjectProviderType

class ModrinthProjectAdapter(project: ModrinthProjectResult) : Project {
    override val provider = ProjectProviderType.Modrinth
    override val id = project.projectId
    override val slug = project.slug
    override val name = project.title
    override val author = project.author
    override val description = project.description
    override val url = "https://modrinth.com/${project.projectType.value}/${project.slug}"
    override val logoUrl = project.iconUrl
    override val imageUrl = project.featuredGallery ?: project.gallery?.firstOrNull()
    override val downloads = project.downloads
    override val lastUpdated = project.dateModified
}

fun List<ModrinthProjectResult>.convertAll(): List<Project> = map { it.convert() }

fun ModrinthProjectResult.convert(): Project = ModrinthProjectAdapter(this)
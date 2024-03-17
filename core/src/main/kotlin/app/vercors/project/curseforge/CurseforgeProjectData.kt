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

package app.vercors.project.curseforge

import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import app.vercors.project.curseforge.data.CurseforgeProject

class CurseforgeProjectData(project: CurseforgeProject) : ProjectData {
    override val provider = ProjectProviderType.Curseforge
    override val id = project.id.toString()
    override val slug = project.slug
    override val name = project.name
    override val author = project.authors[0].name
    override val description = project.summary
    override val url = project.links.websiteUrl
    override val logoUrl = project.logo.thumbnailUrl
    override val imageUrl = project.screenshots.firstOrNull()?.thumbnailUrl
    override val downloads = project.downloadCount
    override val lastUpdated = project.dateModified
    //override val modLoaders = mod.latestFilesIndexes.mapNotNull { it.modLoader }.distinct().toModLoadersCurseforge()
}

fun List<CurseforgeProject>.convertAll(): List<ProjectData> = map { it.convert() }

fun CurseforgeProject.convert(): ProjectData = CurseforgeProjectData(this)
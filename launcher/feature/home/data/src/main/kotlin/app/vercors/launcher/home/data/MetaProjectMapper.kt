/*
 * Copyright (c) 2025 skyecodes
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

package app.vercors.launcher.home.data

import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectId
import app.vercors.launcher.project.domain.ProjectProvider
import app.vercors.launcher.project.domain.ProjectType
import app.vercors.meta.project.MetaProject
import app.vercors.meta.project.MetaProjectProvider
import app.vercors.meta.project.MetaProjectType
import kotlinx.datetime.Instant

fun MetaProject.toDomain(): Project = Project(
    id = ProjectId(provider.toDomain(), id),
    name = name,
    author = author,
    description = description,
    type = type.toDomain(),
    iconUrl = if (hasLogoUrl()) logoUrl else null,
    bannerUrl = if (hasImageUrl()) imageUrl else null,
    downloads = downloads,
    lastUpdated = Instant.fromEpochSeconds(lastUpdated.seconds)
)

fun MetaProjectProvider.toDomain(): ProjectProvider = when (this) {
    MetaProjectProvider.modrinth -> ProjectProvider.Modrinth
    MetaProjectProvider.curseforge -> ProjectProvider.CurseForge
    MetaProjectProvider.UNRECOGNIZED -> throw IllegalArgumentException("Unrecognized ProjectProvider: $this")
}

fun MetaProjectType.toDomain(): ProjectType = when (this) {
    MetaProjectType.mod -> ProjectType.Mod
    MetaProjectType.modpack -> ProjectType.Modpack
    MetaProjectType.resourcepack -> ProjectType.ResourcePack
    MetaProjectType.shader -> ProjectType.ShaderPack
    MetaProjectType.UNRECOGNIZED -> throw IllegalArgumentException("Unrecognized ProjectType: $this")
}

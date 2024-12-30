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

package app.vercors.launcher.project.data.remote.curseforge

import app.vercors.launcher.project.data.remote.curseforge.dto.CurseforgeClassId
import app.vercors.launcher.project.data.remote.curseforge.dto.CurseforgeClassIds
import app.vercors.launcher.project.domain.ProjectType

fun CurseforgeClassId?.toProjectType(): ProjectType = when (this) {
    CurseforgeClassIds.MODPACK -> ProjectType.Modpack
    CurseforgeClassIds.MOD -> ProjectType.Mod
    CurseforgeClassIds.RESOURCEPACK -> ProjectType.ResourcePack
    CurseforgeClassIds.SHADERPACK -> ProjectType.ShaderPack
    else -> throw IllegalArgumentException("Unknown Curseforge classId: $this")
}

fun ProjectType.toClassId(): CurseforgeClassId = when (this) {
    ProjectType.Mod -> CurseforgeClassIds.MOD
    ProjectType.Modpack -> CurseforgeClassIds.MODPACK
    ProjectType.ResourcePack -> CurseforgeClassIds.RESOURCEPACK
    ProjectType.ShaderPack -> CurseforgeClassIds.SHADERPACK
}
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

package app.vercors.launcher.home.data

import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.home.domain.HomeSectionType
import app.vercors.meta.project.MetaProjectType

fun HomeSectionConfig.toType(): HomeSectionType = when (this) {
    HomeSectionConfig.JumpBackIn -> HomeSectionType.JumpBackIn
    HomeSectionConfig.PopularMods -> HomeSectionType.PopularMods
    HomeSectionConfig.PopularModpacks -> HomeSectionType.PopularModpacks
    HomeSectionConfig.PopularResourcePacks -> HomeSectionType.PopularResourcePacks
    HomeSectionConfig.PopularShaderPacks -> HomeSectionType.PopularShaderPacks
}

fun MetaProjectType.toSectionType(): HomeSectionType = when (this) {
    MetaProjectType.mod -> HomeSectionType.PopularMods
    MetaProjectType.modpack -> HomeSectionType.PopularModpacks
    MetaProjectType.resourcepack -> HomeSectionType.PopularResourcePacks
    MetaProjectType.shader -> HomeSectionType.PopularShaderPacks
    MetaProjectType.UNRECOGNIZED -> throw IllegalStateException("Unrecognized HomeSectionType: $this")
}
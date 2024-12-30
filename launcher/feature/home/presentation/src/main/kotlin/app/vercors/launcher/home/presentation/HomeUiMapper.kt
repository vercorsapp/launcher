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

package app.vercors.launcher.home.presentation

import app.vercors.launcher.core.presentation.ui.countToString
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.home.domain.HomeSection
import app.vercors.launcher.home.domain.HomeSectionData
import app.vercors.launcher.home.domain.HomeSectionType
import app.vercors.launcher.instance.domain.Instance
import app.vercors.launcher.project.domain.Project
import kotlinx.datetime.toJavaInstant
import org.jetbrains.compose.resources.StringResource

fun HomeSection.toUi(): HomeSectionUi {
    val title = type.toUi()
    return when (this) {
        is HomeSection.Instances -> {
            val uiData = when (val dat = data) {
                is HomeSectionData.Loaded -> HomeSectionDataUi.Loaded(dat.items.map { it.toUi() })
                is HomeSectionData.Loading -> HomeSectionDataUi.Loading()
            }
            HomeSectionUi.Instances(title, uiData)
        }

        is HomeSection.Projects -> {
            val uiData = when (val dat = data) {
                is HomeSectionData.Loaded -> HomeSectionDataUi.Loaded(dat.items.map { it.toUi() })
                is HomeSectionData.Loading -> HomeSectionDataUi.Loading()
            }
            HomeSectionUi.Projects(title, uiData)
        }
    }
}

private fun Instance.toUi(): HomeSectionItemUi.Instance = HomeSectionItemUi.Instance(
    id = id,
    name = name,
    loader = modLoader?.type?.name ?: "Vanilla",
    gameVersion = gameVersion,
    status = HomeInstanceStatusUi.Running
)

private fun Project.toUi(): HomeSectionItemUi.Project = HomeSectionItemUi.Project(
    id = id,
    name = name,
    author = author,
    iconUrl = iconUrl,
    imageUrl = bannerUrl,
    description = description,
    downloadCount = downloads.countToString(),
    lastUpdated = lastUpdated.toJavaInstant()
)

private fun HomeSectionType.toUi(): StringResource = when (this) {
    HomeSectionType.JumpBackIn -> appString { jump_back_in }
    HomeSectionType.PopularMods -> appString { popular_mods }
    HomeSectionType.PopularModpacks -> appString { popular_modpacks }
    HomeSectionType.PopularResourcePacks -> appString { popular_resource_packs }
    HomeSectionType.PopularShaderPacks -> appString { popular_shader_packs }
}

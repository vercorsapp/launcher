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

package app.vercors.launcher.home.presentation

import app.vercors.launcher.core.presentation.ui.countToString
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.home.domain.HomeSection
import app.vercors.launcher.home.domain.HomeSectionType
import app.vercors.launcher.instance.domain.Instance
import app.vercors.launcher.project.domain.Project
import app.vercors.lib.domain.Resource
import kotlinx.datetime.toJavaInstant
import org.jetbrains.compose.resources.StringResource

fun HomeSection.toUi(): HomeSectionUi {
    val title = type.toUi()
    return when (this) {
        is HomeSection.Instances -> HomeSectionUi.Instances(title, HomeSectionDataUi.Loaded(data.map { it.toUi() }))
        is HomeSection.Projects -> {
            val uiData = when (val finalData = data) {
                Resource.Loading -> HomeSectionDataUi.Loading()
                is Resource.Success -> HomeSectionDataUi.Loaded(finalData.value.map { it.toUi() })
                is Resource.Error -> HomeSectionDataUi.Loading() // TODO error handling
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
    description = description,
    iconUrl = iconUrl,
    imageUrl = bannerUrl,
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

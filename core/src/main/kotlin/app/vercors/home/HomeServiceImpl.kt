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

package app.vercors.home

import app.vercors.instance.InstanceLoadingState
import app.vercors.instance.InstanceService
import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import app.vercors.project.curseforge.CurseforgeService
import app.vercors.project.modrinth.ModrinthService
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

class HomeServiceImpl(
    private val instanceService: InstanceService,
    private val modrinthService: ModrinthService,
    private val curseforgeService: CurseforgeService
) : HomeService {
    override suspend fun loadProjects(
        sectionType: HomeSectionType,
        provider: ProjectProviderType
    ): HomeSection.Projects =
        HomeSection.Projects(
            sectionType, when (sectionType) {
                HomeSectionType.PopularMods -> fetch(provider, HomeProvider::getPopularMods)
                HomeSectionType.PopularModpacks -> fetch(provider, HomeProvider::getPopularModpacks)
                HomeSectionType.PopularResourcePacks -> fetch(provider, HomeProvider::getPopularResourcePacks)
                HomeSectionType.PopularShaderPacks -> fetch(provider, HomeProvider::getPopularShaderPacks)
                else -> throw IllegalArgumentException("Section type $sectionType not supported by this method")
            }
        )

    private suspend fun fetch(
        provider: ProjectProviderType,
        func: suspend (HomeProvider) -> List<ProjectData>
    ): List<ProjectData> = func(
        when (provider) {
            ProjectProviderType.Modrinth -> modrinthService
            ProjectProviderType.Curseforge -> curseforgeService
        }
    )

    override suspend fun loadInstances(): HomeSection.Instances = HomeSection.Instances(
        instanceService.loadingState
            .filterIsInstance<InstanceLoadingState.Loaded>().first()
            .instances.sortedByDescending { it.lastPlayed ?: it.dateCreated }
    )
}
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

import app.vercors.common.Resource
import app.vercors.instance.Instance
import app.vercors.instance.InstanceRepository
import app.vercors.project.Project
import app.vercors.project.ProjectProviderType
import app.vercors.project.ProjectRepository
import app.vercors.project.curseforge.CurseforgeProjectRepository
import app.vercors.project.modrinth.ModrinthProjectRepository
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

internal class LoadHomeSectionUseCaseImpl(
    private val instanceRepository: InstanceRepository,
    private val modrinthProjectRepository: ModrinthProjectRepository,
    private val curseforgeProjectRepository: CurseforgeProjectRepository
) : LoadHomeSectionUseCase {
    override suspend fun loadProjects(
        sectionType: HomeSectionType,
        provider: ProjectProviderType
    ): HomeSection.Projects =
        HomeSection.Projects(
            sectionType, when (sectionType) {
                HomeSectionType.PopularMods -> fetch(provider, ProjectRepository::getPopularMods)
                HomeSectionType.PopularModpacks -> fetch(provider, ProjectRepository::getPopularModpacks)
                HomeSectionType.PopularResourcePacks -> fetch(provider, ProjectRepository::getPopularResourcePacks)
                HomeSectionType.PopularShaderPacks -> fetch(provider, ProjectRepository::getPopularShaderPacks)
                else -> throw IllegalArgumentException("Section type $sectionType not supported by this method")
            }
        )

    private suspend fun fetch(
        provider: ProjectProviderType,
        func: suspend (ProjectRepository) -> List<Project>
    ): List<Project> = func(
        when (provider) {
            ProjectProviderType.Modrinth -> modrinthProjectRepository
            ProjectProviderType.Curseforge -> curseforgeProjectRepository
        }
    )

    override suspend fun loadInstances(): HomeSection.Instances = HomeSection.Instances(
        instanceRepository.loadingState
            .filterIsInstance<Resource.Loaded<List<Instance>>>().first()
            .result.sortedByDescending { it.data.lastPlayed ?: it.data.dateCreated }
    )
}
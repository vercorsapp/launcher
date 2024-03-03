package app.vercors.home

import app.vercors.instance.InstanceLoadingState
import app.vercors.instance.InstanceService
import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import app.vercors.project.curseforge.CurseforgeService
import app.vercors.project.modrinth.ModrinthService
import kotlinx.collections.immutable.toImmutableList
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
            }.toImmutableList()
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
            .instances.sortedByDescending { it.lastPlayed ?: it.dateCreated }.toImmutableList()
    )
}
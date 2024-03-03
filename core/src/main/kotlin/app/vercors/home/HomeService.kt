package app.vercors.home

import app.vercors.project.ProjectProviderType

interface HomeService {
    suspend fun loadProjects(
        sectionType: HomeSectionType,
        provider: ProjectProviderType
    ): HomeSection.Projects


    suspend fun loadInstances(): HomeSection.Instances
}

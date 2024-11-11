package app.vercors.launcher.project.data.remote.modrinth.datasource

import app.vercors.launcher.project.data.remote.datasource.ProviderDataSource
import app.vercors.launcher.project.data.remote.modrinth.ModrinthApi
import app.vercors.launcher.project.data.remote.modrinth.mapper.toModrinthProjectType
import app.vercors.launcher.project.data.remote.modrinth.mapper.toProject
import app.vercors.launcher.project.domain.model.Project
import app.vercors.launcher.project.domain.model.ProjectType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ModrinthDataSource(private val api: ModrinthApi) : ProviderDataSource {
    override fun findProjects(projectType: ProjectType, query: String?, limit: Int): Flow<List<Project>> =
        api.search(
            query = query,
            facets = buildFacets(projectType),
            limit = limit
        ).map { result -> result.hits.map { it.toProject() } }

    private fun buildFacets(projectType: ProjectType): String =
        "[[\"project_type:${projectType.toModrinthProjectType().value}\"]]"
}



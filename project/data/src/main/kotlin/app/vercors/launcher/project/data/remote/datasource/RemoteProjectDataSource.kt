package app.vercors.launcher.project.data.remote.datasource

import app.vercors.launcher.project.data.remote.curseforge.datasource.CurseforgeDataSource
import app.vercors.launcher.project.data.remote.modrinth.datasource.ModrinthDataSource
import app.vercors.launcher.project.domain.model.Project
import app.vercors.launcher.project.domain.model.ProjectProvider
import app.vercors.launcher.project.domain.model.ProjectType
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class RemoteProjectDataSource(
    private val modrinthDataSource: ModrinthDataSource,
    private val curseforgeDataSource: CurseforgeDataSource,
) {
    fun findProjects(
        provider: ProjectProvider,
        projectType: ProjectType,
        query: String?,
        limit: Int
    ): Flow<List<Project>> =
        provider.getDataSource().findProjects(projectType, query, limit)

    private fun ProjectProvider.getDataSource(): ProviderDataSource = when (this) {
        ProjectProvider.Modrinth -> modrinthDataSource
        ProjectProvider.Curseforge -> curseforgeDataSource
    }
}
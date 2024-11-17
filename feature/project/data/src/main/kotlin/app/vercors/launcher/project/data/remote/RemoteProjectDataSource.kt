package app.vercors.launcher.project.data.remote

import app.vercors.launcher.project.data.remote.curseforge.CurseforgeDataSource
import app.vercors.launcher.project.data.remote.modrinth.ModrinthDataSource
import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectProvider
import app.vercors.launcher.project.domain.ProjectType
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
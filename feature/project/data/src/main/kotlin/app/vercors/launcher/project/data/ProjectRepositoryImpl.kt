package app.vercors.launcher.project.data

import app.vercors.launcher.project.data.local.LocalProjectDataSource
import app.vercors.launcher.project.data.remote.RemoteProjectDataSource
import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectProvider
import app.vercors.launcher.project.domain.ProjectRepository
import app.vercors.launcher.project.domain.ProjectType
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class ProjectRepositoryImpl(
    private val localProjectDataSource: LocalProjectDataSource,
    private val remoteProjectDataSource: RemoteProjectDataSource
) : ProjectRepository {
    override fun findProjects(
        provider: ProjectProvider,
        type: ProjectType,
        query: String?,
        limit: Int
    ): Flow<List<Project>> {
        return remoteProjectDataSource.findProjects(provider, type, query, limit)
    }
}
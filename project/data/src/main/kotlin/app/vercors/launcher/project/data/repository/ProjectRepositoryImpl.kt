package app.vercors.launcher.project.data.repository

import app.vercors.launcher.project.data.local.datasource.LocalProjectDataSource
import app.vercors.launcher.project.data.remote.datasource.RemoteProjectDataSource
import app.vercors.launcher.project.domain.model.Project
import app.vercors.launcher.project.domain.model.ProjectProvider
import app.vercors.launcher.project.domain.model.ProjectType
import app.vercors.launcher.project.domain.repository.ProjectRepository
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
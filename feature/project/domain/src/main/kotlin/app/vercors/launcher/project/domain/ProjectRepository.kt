package app.vercors.launcher.project.domain

import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun findProjects(
        provider: ProjectProvider,
        type: ProjectType,
        query: String? = null,
        limit: Int = 20,
    ): Flow<List<Project>>
}
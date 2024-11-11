package app.vercors.launcher.project.domain.repository

import app.vercors.launcher.project.domain.model.Project
import app.vercors.launcher.project.domain.model.ProjectProvider
import app.vercors.launcher.project.domain.model.ProjectType
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun findProjects(
        provider: ProjectProvider,
        type: ProjectType,
        query: String? = null,
        limit: Int = 20,
    ): Flow<List<Project>>
}
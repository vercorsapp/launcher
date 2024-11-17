package app.vercors.launcher.project.data.remote

import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectType
import kotlinx.coroutines.flow.Flow

interface ProviderDataSource {
    fun findProjects(projectType: ProjectType, query: String?, limit: Int): Flow<List<Project>>
}
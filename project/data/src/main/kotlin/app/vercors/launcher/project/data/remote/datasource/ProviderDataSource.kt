package app.vercors.launcher.project.data.remote.datasource

import app.vercors.launcher.project.domain.model.Project
import app.vercors.launcher.project.domain.model.ProjectType
import kotlinx.coroutines.flow.Flow

interface ProviderDataSource {
    fun findProjects(projectType: ProjectType, query: String?, limit: Int): Flow<List<Project>>
}
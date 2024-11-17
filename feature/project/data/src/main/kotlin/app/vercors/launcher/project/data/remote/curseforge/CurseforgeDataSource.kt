package app.vercors.launcher.project.data.remote.curseforge

import app.vercors.launcher.project.data.remote.ProviderDataSource
import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class CurseforgeDataSource(private val api: CurseforgeApi) : ProviderDataSource {
    override fun findProjects(projectType: ProjectType, query: String?, limit: Int): Flow<List<Project>> =
        api.search(
            gameId = MINECRAFT_GAME_ID,
            classId = projectType.toClassId(),
            pageSize = limit
        ).map { result -> result.data.map { it.toProject() } }

    companion object {
        private const val MINECRAFT_GAME_ID = 432
    }
}



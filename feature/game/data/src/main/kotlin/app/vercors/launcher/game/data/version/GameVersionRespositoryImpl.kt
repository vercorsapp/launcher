package app.vercors.launcher.game.data.version

import app.vercors.launcher.core.domain.DomainError
import app.vercors.launcher.core.domain.Resource
import app.vercors.launcher.core.domain.map
import app.vercors.launcher.core.domain.observeResource
import app.vercors.launcher.core.meta.game.GameApi
import app.vercors.launcher.game.domain.version.GameVersionList
import app.vercors.launcher.game.domain.version.GameVersionRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GameVersionRespositoryImpl(
    private val gameApi: GameApi,
) : GameVersionRepository {
    override fun observeVersionList(): Flow<Resource<GameVersionList, DomainError.Remote>> =
        observeResource { gameApi.getGameVersions().map { it.toGameVersionList() } }
}
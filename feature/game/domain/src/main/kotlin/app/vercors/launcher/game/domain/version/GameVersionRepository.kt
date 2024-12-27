package app.vercors.launcher.game.domain.version

import app.vercors.launcher.core.domain.DomainError
import app.vercors.launcher.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface GameVersionRepository {
    fun observeVersionList(): Flow<Resource<GameVersionList, DomainError.Remote>>
}
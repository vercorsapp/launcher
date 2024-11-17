package app.vercors.launcher.game.domain.version

import kotlinx.coroutines.flow.Flow

interface GameVersionRepository {
    fun observeVersionList(): Flow<GameVersionList>
}
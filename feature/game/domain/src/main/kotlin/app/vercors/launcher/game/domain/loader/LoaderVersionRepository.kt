package app.vercors.launcher.game.domain.loader

import app.vercors.launcher.core.domain.DomainError
import app.vercors.launcher.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface LoaderVersionRepository {
    fun observeLoaderVersions(
        loader: ModLoaderType,
        gameVersion: String
    ): Flow<Resource<List<LoaderVersion>, DomainError>>
}
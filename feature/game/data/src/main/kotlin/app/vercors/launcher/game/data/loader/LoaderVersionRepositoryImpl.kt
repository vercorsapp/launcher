package app.vercors.launcher.game.data.loader

import app.vercors.launcher.core.domain.DomainError
import app.vercors.launcher.core.domain.Resource
import app.vercors.launcher.core.domain.map
import app.vercors.launcher.core.domain.observeResource
import app.vercors.launcher.core.meta.loader.LoaderApi
import app.vercors.launcher.game.data.toStringData
import app.vercors.launcher.game.domain.loader.LoaderVersion
import app.vercors.launcher.game.domain.loader.LoaderVersionRepository
import app.vercors.launcher.game.domain.loader.ModLoaderType
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LoaderVersionRepositoryImpl(
    val loaderApi: LoaderApi
) : LoaderVersionRepository {
    override fun observeLoaderVersions(
        loader: ModLoaderType,
        gameVersion: String
    ): Flow<Resource<List<LoaderVersion>, DomainError>> = observeResource {
        loaderApi.getLoaderVersions(loader.toStringData(), gameVersion).map { it.toLoaderVersionList() }
    }
}
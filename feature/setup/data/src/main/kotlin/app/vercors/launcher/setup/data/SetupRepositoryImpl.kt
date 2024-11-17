package app.vercors.launcher.setup.data

import app.vercors.launcher.core.storage.Storage
import app.vercors.launcher.setup.domain.SetupRepository
import ca.gosyer.appdirs.AppDirs
import org.koin.core.annotation.Single

@Single
class SetupRepositoryImpl(
    appDirs: AppDirs,
    private val storage: Storage
) : SetupRepository {
    override val defaultPath: String = appDirs.getUserDataDir()
    override fun updatePath(path: String) = storage.updatePath(path)
}
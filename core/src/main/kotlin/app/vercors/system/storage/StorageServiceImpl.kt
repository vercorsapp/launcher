package app.vercors.system.storage

import app.vercors.appBasePath
import app.vercors.instance.InstanceData
import java.nio.file.Path

class StorageServiceImpl : StorageService {
    override val basePath: Path get() = appBasePath!!
    override val configPath: Path get() = basePath.resolve("config.json")
    private val cachePath: Path get() = basePath.resolve("cache")
    override val coilCachePath: Path get() = cachePath.resolve("coil")
    override val instancesPath: Path get() = basePath.resolve("instances")
    private val versionsDir: Path get() = basePath.resolve("versions")
    override val assetsPath: Path get() = basePath.resolve("assets")
    private val assetIndexDir: Path get() = assetsPath.resolve("indexes")
    private val assetObjectsDir: Path get() = assetsPath.resolve("objects")
    private val librariesDir: Path get() = basePath.resolve("libraries")
    private val loggingDir: Path get() = basePath.resolve("logging")

    override fun getInstancePath(instance: InstanceData): Path = instancesPath.resolve(instance.path)

    override fun getInstanceNativesPath(instance: InstanceData): Path = getInstancePath(instance).resolve("natives")

    private fun getVersionPath(versionId: String): Path = versionsDir.resolve(versionId)

    override fun getVersionJsonPath(versionId: String): Path = getVersionPath(versionId).resolve("$versionId.json")

    override fun getVersionJarPath(versionId: String): Path = getVersionPath(versionId).resolve("$versionId.jar")

    override fun getLibraryPath(path: String): Path = librariesDir.resolve(path)

    override fun getAssetIndexPath(assets: String): Path = assetIndexDir.resolve("$assets.json")

    override fun getAssetPath(sha1: String): Path = assetObjectsDir.resolve(sha1.take(2)).resolve(sha1)

    override fun getLogConfigPath(type: String, id: String): Path = loggingDir.resolve(type).resolve(id)
}
package com.skyecodes.vercors.common

import com.skyecodes.vercors.APP_NAME
import com.skyecodes.vercors.instances.Instance
import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Path
import java.nio.file.Paths

interface StorageService {
    //val cacheDir: Path
    //val baseDir: Path
    val configDir: Path
    val instancesDir: Path
    val assetsDir: Path
    //val librariesDir: Path

    fun getInstanceDir(instance: Instance): Path
    fun getInstanceNativesDir(instance: Instance): Path
    fun getVersionJsonPath(versionId: String): Path
    fun getVersionJarPath(versionId: String): Path
    fun getLibraryPath(path: String): Path
    fun getAssetIndexPath(assets: String): Path
    fun getAssetPath(sha1: String): Path
    fun getLogConfigPath(type: String, id: String): Path
}

class StorageServiceImpl : StorageService {
    private val appDirs = AppDirsFactory.getInstance()

    //override val cacheDir: Path = Paths.get(appDirs.getUserCacheDir(APP_NAME, null, null))
    private val baseDir: Path = Paths.get(appDirs.getUserDataDir(APP_NAME, null, null))
    override val configDir: Path = Paths.get(appDirs.getUserConfigDir(APP_NAME, null, null))
    override val instancesDir: Path = baseDir.resolve("instances")
    private val versionsDir: Path = baseDir.resolve("versions")
    override val assetsDir: Path = baseDir.resolve("assets")
    private val assetIndexDir: Path = assetsDir.resolve("indexes")
    private val assetObjectsDir: Path = assetsDir.resolve("objects")
    private val librariesDir: Path = baseDir.resolve("libraries")
    private val loggingDir: Path = baseDir.resolve("logging")

    override fun getInstanceDir(instance: Instance): Path = instancesDir.resolve(instance.path)

    override fun getInstanceNativesDir(instance: Instance): Path = getInstanceDir(instance).resolve("natives")

    private fun getVersionDir(versionId: String): Path = versionsDir.resolve(versionId)

    override fun getVersionJsonPath(versionId: String): Path = getVersionDir(versionId).resolve("$versionId.json")

    override fun getVersionJarPath(versionId: String): Path = getVersionDir(versionId).resolve("$versionId.jar")

    override fun getLibraryPath(path: String): Path = librariesDir.resolve(path)

    override fun getAssetIndexPath(assets: String): Path = assetIndexDir.resolve("$assets.json")

    override fun getAssetPath(sha1: String): Path = assetObjectsDir.resolve(sha1.take(2)).resolve(sha1)

    override fun getLogConfigPath(type: String, id: String): Path = loggingDir.resolve(type).resolve(id)
}
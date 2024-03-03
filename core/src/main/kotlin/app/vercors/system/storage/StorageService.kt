package app.vercors.system.storage

import app.vercors.instance.InstanceData
import java.nio.file.Path

interface StorageService {
    val defaultBasePath: Path
    val basePath: Path
    val cachePath: Path
    val instancesPath: Path
    val assetsPath: Path

    fun getInstancePath(instance: InstanceData): Path
    fun getInstanceNativesPath(instance: InstanceData): Path
    fun getVersionJsonPath(versionId: String): Path
    fun getVersionJarPath(versionId: String): Path
    fun getLibraryPath(path: String): Path
    fun getAssetIndexPath(assets: String): Path
    fun getAssetPath(sha1: String): Path
    fun getLogConfigPath(type: String, id: String): Path
}
/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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

    override fun getInstancePath(instance: InstanceData): Path = instancesPath.resolve(instance.id)

    override fun getInstanceNativesPath(instance: InstanceData): Path = getInstancePath(instance).resolve("natives")

    private fun getVersionPath(versionId: String): Path = versionsDir.resolve(versionId)

    override fun getVersionJsonPath(versionId: String): Path = getVersionPath(versionId).resolve("$versionId.json")

    override fun getVersionJarPath(versionId: String): Path = getVersionPath(versionId).resolve("$versionId.jar")

    override fun getLibraryPath(path: String): Path = librariesDir.resolve(path)

    override fun getAssetIndexPath(assets: String): Path = assetIndexDir.resolve("$assets.json")

    override fun getAssetPath(sha1: String): Path = assetObjectsDir.resolve(sha1.take(2)).resolve(sha1)

    override fun getLogConfigPath(type: String, id: String): Path = loggingDir.resolve(type).resolve(id)
}
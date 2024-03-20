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

package app.vercors.system

import java.nio.file.Path

interface StorageManager {
    val basePath: Path
    val configPath: Path
    val accountsPath: Path
    val coilCachePath: Path
    val instancesPath: Path
    val assetsPath: Path

    fun getInstancePath(id: String): Path
    fun getInstanceConfigPath(id: String): Path
    fun getInstanceNativesPath(id: String): Path
    fun getVersionJsonPath(versionId: String): Path
    fun getVersionJarPath(versionId: String): Path
    fun getLibraryPath(path: String): Path
    fun getAssetIndexPath(assets: String): Path
    fun getAssetPath(sha1: String): Path
    fun getLogConfigPath(type: String, id: String): Path
}
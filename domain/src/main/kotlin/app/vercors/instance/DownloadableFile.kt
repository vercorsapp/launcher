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

package app.vercors.instance

import app.vercors.instance.mojang.MojangFile
import java.nio.file.Path

sealed interface DownloadableFile {
    val path: Path
    val url: String
    val size: Int
    val sha1: String

    abstract class Basic(override val path: Path, mojangFile: MojangFile) : DownloadableFile {
        override val url = mojangFile.url
        override val size = mojangFile.size
        override val sha1 = mojangFile.sha1
    }

    class Client(path: Path, mojangFile: MojangFile) : Basic(path, mojangFile)

    class Library(path: Path, mojangFile: MojangFile) : Basic(path, mojangFile)

    class Native(
        path: Path,
        mojangFile: MojangFile,
        val dest: Path,
        val excludes: List<String>
    ) : Basic(path, mojangFile)

    class Asset(
        override val path: Path,
        override val url: String,
        override val size: Int,
        override val sha1: String,
    ) : DownloadableFile

    class LogConfig(path: Path, mojangFile: MojangFile) : Basic(path, mojangFile)
}
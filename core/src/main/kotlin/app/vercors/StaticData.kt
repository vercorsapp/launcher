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

package app.vercors

import java.nio.file.Path
import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.inputStream
import kotlin.io.path.isRegularFile
import kotlin.io.path.outputStream

object StaticData {
    private val appDataFile = Path.of("vercors.properties")
    private val appDataFileExists: Boolean get() = appDataFile.isRegularFile()

    private var _appDataPath: Path? = null
    var appDataPath: Path?
        get() {
            if (_appDataPath == null && appDataFileExists) {
                _appDataPath =
                    Path.of(Properties().apply { appDataFile.inputStream().use { load(it) } }.getProperty("path"))
            }
            return _appDataPath
        }
        set(value) {
            if (value != null) {
                Properties().apply {
                    set("path", value.absolutePathString())
                    appDataFile.outputStream().use { store(it, null) }
                }
                _appDataPath = value
            }
        }
    val logsFolder: Path get() = appDataPath?.resolve("logs") ?: Path.of("logs")
}

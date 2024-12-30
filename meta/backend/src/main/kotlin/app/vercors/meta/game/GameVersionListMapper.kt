/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.meta.game

import app.vercors.meta.game.mojang.MojangReleaseType
import app.vercors.meta.game.mojang.MojangVersion
import app.vercors.meta.game.mojang.MojangVersionManifest

fun MojangVersionManifest.toResult(): MetaGameVersionList = let {
    metaGameVersionList {
        recommended = it.latest.release
        latest = it.latest.snapshot
        versions += it.versions.map { it.toResult() }
    }
}

private fun MojangVersion.toResult(): MetaGameVersion = let {
    metaGameVersion {
        id = it.id
        type = it.type.toResult()
        time = it.time.toEpochMilli()
        url = it.url
        sha1 = it.sha1
    }
}

private fun MojangReleaseType.toResult(): MetaGameVersionType = when (this) {
    MojangReleaseType.Release -> MetaGameVersionType.release
    MojangReleaseType.Snapshot -> MetaGameVersionType.snapshot
    MojangReleaseType.Beta -> MetaGameVersionType.beta
    MojangReleaseType.Alpha -> MetaGameVersionType.alpha
}
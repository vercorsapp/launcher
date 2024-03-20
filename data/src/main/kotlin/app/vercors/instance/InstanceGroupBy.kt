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

import app.vercors.common.ModLoader
import app.vercors.instance.mojang.MojangVersionManifest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class InstanceGroupBy(val behavior: Behavior<*>) {
    @SerialName("none")
    None(Behavior.None),

    @SerialName("gameVersion")
    GameVersion(Behavior.GameVersion),

    @SerialName("loader")
    Loader(Behavior.Loader);

    sealed class Behavior<T>(
        val key: (Instance) -> T,
        val header: (T) -> String = { it.toString() },
        val comparator: Comparator<T> = Comparator.comparing { header(it) }
    ) {
        data object None : Behavior<String>(
            key = { "" }
        )

        data object GameVersion : Behavior<MojangVersionManifest.Version>(
            key = { it.data.gameVersion },
            header = MojangVersionManifest.Version::id,
            comparator = Comparator.comparing(MojangVersionManifest.Version::releaseTime)
        )

        data object Loader : Behavior<ModLoader?>(
            key = { it.data.loader },
            header = { it?.text ?: ModLoader.Vanilla }
        )
    }
}
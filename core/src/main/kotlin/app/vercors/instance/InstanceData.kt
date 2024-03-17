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

import app.vercors.common.AppDuration
import app.vercors.common.AppInstant
import app.vercors.instance.mojang.data.MojangVersionManifest
import app.vercors.project.ModLoader
import kotlinx.coroutines.Job
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.Duration
import java.time.Instant

@Serializable
data class InstanceData(
    val version: Int = 0,
    val name: String,
    val id: String = "",
    val icon: Icon? = null,
    val gameVersion: MojangVersionManifest.Version,
    val loader: ModLoader? = null,
    val loaderVersion: String? = null,
    val dateCreated: AppInstant = Instant.now(),
    val dateModified: AppInstant = Instant.now(),
    val lastPlayed: AppInstant? = null,
    val timePlayed: AppDuration = Duration.ZERO,
    @Transient
    var dirty: Boolean = false,
    @Transient
    val status: InstanceStatus = InstanceStatus.Stopped,
    @Transient
    val preparationJob: Job? = null,
    @Transient
    val runJob: Job? = null
) {

    @Serializable
    data class Icon(
        val type: Type,
        val name: String
    ) {
        @Serializable
        enum class Type {
            @SerialName("base")
            Base,

            @SerialName("custom")
            Custom
        }
    }
}

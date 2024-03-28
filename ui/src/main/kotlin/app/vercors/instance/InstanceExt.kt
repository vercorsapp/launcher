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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.LocalPalette
import app.vercors.project.string
import app.vercors.readable
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.*

val Instance.loaderAndVersionString: String @Stable get() = "${data.loader.string} ${data.gameVersion.id}"

val Instance.lastPlayedString: String
    @Composable get() = data.lastPlayed
        ?.let { stringResource(Res.string.lastPlayedTime, it.readable()) }
        ?: stringResource(Res.string.notPlayedBefore)


val Instance.timePlayedString: String
    @Composable get() = data.timePlayed
        .let {
            if (it.isZero) stringResource(Res.string.notPlayedBefore)
            else stringResource(Res.string.timePlayed, it.readable())
        }

val Instance.statusIcon: ImageVector
    @Composable @Stable get() = when (status) {
        is InstanceStatus.Running -> FeatherIcons.PlayCircle
        InstanceStatus.NotRunning -> FeatherIcons.StopCircle
        InstanceStatus.Crashed -> FeatherIcons.AlertOctagon
        InstanceStatus.Killed -> FeatherIcons.XCircle
        else -> FeatherIcons.CheckCircle
    }

val Instance.statusColor: Color
    @Composable get() = when (status) {
        is InstanceStatus.Running -> LocalPalette.current.green
        InstanceStatus.NotRunning, InstanceStatus.Crashed, InstanceStatus.Killed -> LocalPalette.current.red
        else -> LocalPalette.current.yellow
    }

val Instance.statusText: String
    @Composable get() =
        when (status) {
            is InstanceStatus.Running -> stringResource(Res.string.running)
            InstanceStatus.NotRunning -> stringResource(Res.string.notRunning)
            InstanceStatus.Crashed -> stringResource(Res.string.crashed)
            InstanceStatus.Killed -> stringResource(Res.string.killed)
            else -> stringResource(Res.string.launching)
        }

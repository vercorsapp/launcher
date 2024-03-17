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

package app.vercors.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.LocalPalette
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertOctagon
import compose.icons.feathericons.AlertTriangle
import compose.icons.feathericons.Info
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.instanceNotFound
import vercors.app.generated.resources.notificationError

val NotificationData.icon: ImageVector
    @Stable get() = when (level) {
        NotificationLevel.INFO -> FeatherIcons.Info
        NotificationLevel.WARN -> FeatherIcons.AlertTriangle
        NotificationLevel.ERROR -> FeatherIcons.AlertOctagon
    }

val NotificationData.color: Color
    @Composable get() = when (level) {
        NotificationLevel.INFO -> LocalPalette.current.blue
        NotificationLevel.WARN -> LocalPalette.current.yellow
        NotificationLevel.ERROR -> LocalPalette.current.red
    }

val NotificationData.actualText: String
    @Composable get() = when (text) {
        is NotificationText.Literal -> (text as NotificationText.Literal).text
        NotificationText.Template.InstanceNotFound -> stringResource(Res.string.instanceNotFound, *args)
        NotificationText.Template.Error -> stringResource(Res.string.notificationError, *args)

    }
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.common.AppIconButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bell
import compose.icons.feathericons.BellOff
import compose.icons.feathericons.Trash2
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.delete
import vercors.ui.generated.resources.markAsRead
import vercors.ui.generated.resources.markAsUnread

@Composable
fun NotificationContent(
    notification: NotificationData,
    onToggleNotificationReadStatus: () -> Unit,
    onClearNotification: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)
    ) {
        Icon(
            imageVector = notification.icon,
            tint = notification.color,
            contentDescription = notification.level.name,
            modifier = Modifier.size(UI.mediumIconSize)
        )
        Text(
            text = notification.actualText,
            style = MaterialTheme.typography.body2,
            fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        AppIconButton(
            imageVector = if (notification.isRead) FeatherIcons.Bell else FeatherIcons.BellOff,
            tooltipText = stringResource(if (notification.isRead) Res.string.markAsUnread else Res.string.markAsRead),
            onClick = onToggleNotificationReadStatus,
            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2)
        )
        AppIconButton(
            imageVector = FeatherIcons.Trash2,
            tooltipText = stringResource(Res.string.delete),
            onClick = onClearNotification,
            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2)
        )
    }
}
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
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.delete
import vercors.app.generated.resources.markAsRead
import vercors.app.generated.resources.markAsUnread

@Composable
fun NotificationContent(
    notification: NotificationData,
    onToggleNotificationReadStatus: (NotificationData) -> Unit,
    onClearNotification: (NotificationData) -> Unit
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
            onClick = { onToggleNotificationReadStatus(notification) },
            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2)
        )
        AppIconButton(
            imageVector = FeatherIcons.Trash2,
            tooltipText = stringResource(Res.string.delete),
            onClick = { onClearNotification(notification) },
            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2)
        )
    }
}
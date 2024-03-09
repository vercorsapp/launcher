package app.vercors.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import app.vercors.UI

@Composable
fun NotificationContent(
    notification: NotificationData,
    onClearNotification: (NotificationData) -> Unit,
    onUpdateNotification: (NotificationData) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
        modifier = Modifier.padding(UI.smallPadding)
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
            fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold
        )
    }
}
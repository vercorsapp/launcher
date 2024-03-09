package app.vercors.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.common.IconTextButton
import app.vercors.common.appAnimateContentSize
import compose.icons.FeatherIcons
import compose.icons.feathericons.BellOff
import compose.icons.feathericons.Trash2
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NotificationListContent(component: NotificationListComponent) {
    val uiState by component.uiState.collectAsState()
    val notificationsCount = uiState.notifications.count()

    if (uiState.isPopupOpen) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset.Zero,
            onDismissRequest = component::onTogglePopup,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier.widthIn(350.dp, 600.dp).heightIn(100.dp, 600.dp)
                    .padding(top = UI.mediumPadding, end = UI.largePadding).shadow(8.dp)
                    .appAnimateContentSize()
            ) {
                Column(Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(
                                if (notificationsCount > 1) Res.string.notificationsCount
                                else Res.string.notificationCount,
                                notificationsCount
                            ),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        IconTextButton(
                            imageVector = FeatherIcons.BellOff,
                            text = stringResource(Res.string.markAllAsRead),
                            onClick = component::markAllNotificationsAsRead,
                            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2),
                            enabled = notificationsCount > 0
                        )
                        IconTextButton(
                            imageVector = FeatherIcons.Trash2,
                            text = stringResource(Res.string.deleteAll),
                            onClick = component::clearAllNotifications,
                            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2),
                            enabled = notificationsCount > 0
                        )
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        modifier = Modifier.padding(UI.mediumPadding)
                    ) {
                        items(
                            items = uiState.notifications,
                            key = NotificationData::id
                        ) {
                            NotificationContent(it, component::clearNotification, component::updateNotification)
                        }
                        if (uiState.notifications.isEmpty()) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = UI.largePadding)
                                ) {
                                    Text(
                                        text = stringResource(Res.string.noNotifications),
                                        style = MaterialTheme.typography.h6
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


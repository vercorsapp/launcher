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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.*

@Composable
fun NotificationListContent(
    state: NotificationListState,
    onIntent: (NotificationListIntent) -> Unit
) {
    val notificationsCount = state.notifications.count()
    val unreadNotificationsCount = state.notifications.count { !it.isRead }

    if (state.isPopupOpen) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset.Zero,
            onDismissRequest = { onIntent(NotificationListIntent.TogglePopup) },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier.width(600.dp).heightIn(100.dp, 600.dp)
                    .padding(top = UI.mediumPadding, end = UI.largePadding).shadow(8.dp)
                    .appAnimateContentSize(),
                backgroundColor = LocalPalette.current.surface0
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                    modifier = Modifier.padding(horizontal = UI.mediumLargePadding, vertical = UI.mediumPadding)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(
                                if (unreadNotificationsCount > 1) Res.string.notificationsCount
                                else if (unreadNotificationsCount == 1) Res.string.notificationCount
                                else Res.string.noNotificationCount,
                                unreadNotificationsCount
                            ),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        IconTextButton(
                            imageVector = FeatherIcons.BellOff,
                            text = stringResource(Res.string.markAllAsRead),
                            onClick = { onIntent(NotificationListIntent.MarkAllNotificationsAsRead) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2),
                            enabled = unreadNotificationsCount > 0
                        )
                        IconTextButton(
                            imageVector = FeatherIcons.Trash2,
                            text = stringResource(Res.string.deleteAll),
                            onClick = { onIntent(NotificationListIntent.ClearAllNotifications) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2),
                            enabled = notificationsCount > 0
                        )
                    }
                    Box {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        ) {
                            items(
                                items = state.notifications,
                                key = NotificationData::id
                            ) {
                                NotificationContent(
                                    notification = it,
                                    onToggleNotificationReadStatus = {
                                        onIntent(
                                            NotificationListIntent.ToggleNotificationReadStatus(
                                                it
                                            )
                                        )
                                    },
                                    onClearNotification = { onIntent(NotificationListIntent.ClearNotification(it)) }
                                )
                            }
                            if (state.notifications.isEmpty()) {
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = UI.mediumLargePadding)
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
}


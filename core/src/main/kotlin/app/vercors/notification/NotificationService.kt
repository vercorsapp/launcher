package app.vercors.notification

import kotlinx.coroutines.flow.StateFlow

interface NotificationService : NotificationEventHandler {
    val notificationsState: StateFlow<List<NotificationData>>
}
package app.vercors.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NotificationServiceImpl : NotificationService {
    private val _notificationsState: MutableStateFlow<List<NotificationData>> = MutableStateFlow(
        buildList {
            repeat(1) {
                addAll(
                    listOf(
                        NotificationData(NotificationLevel.INFO, "Test notification info"),
                        NotificationData(NotificationLevel.WARN, "Test notification warn"),
                        NotificationData(NotificationLevel.ERROR, "Test notification error"),
                    )
                )
            }
        }
    )
    override val notificationsState: StateFlow<List<NotificationData>> = _notificationsState

    override fun sendNotification(notification: NotificationData) {
        _notificationsState.update { it + notification }
    }

    override fun clearNotification(notification: NotificationData) {
        _notificationsState.update { it - notification }
    }

    override fun toggleNotificationReadStatus(notification: NotificationData) {
        _notificationsState.update { state -> state.map { if (it.id == notification.id) notification.copy(isRead = !notification.isRead) else it } }
    }

    override fun clearAllNotifications() {
        _notificationsState.update { emptyList() }
    }

    override fun markAllNotificationsAsRead() {
        _notificationsState.update { state -> state.map { it.copy(isRead = true) } }
    }
}
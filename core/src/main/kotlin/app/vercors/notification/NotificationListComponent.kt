package app.vercors.notification

import kotlinx.coroutines.flow.StateFlow

interface NotificationListComponent : NotificationEventHandler {
    val uiState: StateFlow<NotificationsUiState>

    fun onTogglePopup()
}
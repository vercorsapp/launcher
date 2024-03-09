package app.vercors.notification

data class NotificationsUiState(
    val isPopupOpen: Boolean = false,
    val notifications: List<NotificationData> = emptyList()
)

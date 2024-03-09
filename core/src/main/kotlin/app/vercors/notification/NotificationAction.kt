package app.vercors.notification

data class NotificationAction(
    val text: NotificationText,
    val action: () -> Unit
)

package app.vercors.notification

interface NotificationEventHandler {
    fun sendNotification(notification: NotificationData)
    fun toggleNotificationReadStatus(notification: NotificationData)
    fun clearNotification(notification: NotificationData)
    fun clearAllNotifications()
    fun markAllNotificationsAsRead()
}

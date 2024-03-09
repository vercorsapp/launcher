package app.vercors.notification

interface NotificationEventHandler {
    fun sendNotification(notification: NotificationData)
    fun updateNotification(notification: NotificationData)
    fun clearNotification(notification: NotificationData)
    fun clearAllNotifications()
    fun markAllNotificationsAsRead()
}

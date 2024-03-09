package app.vercors.notification

sealed interface NotificationText {
    data class Literal(val text: String) : NotificationText
    enum class Template : NotificationText {
        InstanceNotFound, Error
    }
}

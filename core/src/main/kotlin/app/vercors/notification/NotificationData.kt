package app.vercors.notification

import java.util.concurrent.atomic.AtomicLong

data class NotificationData(
    val level: NotificationLevel,
    val text: NotificationText,
    val args: Array<Any>,
    val actions: List<NotificationAction> = emptyList(),
    val isRead: Boolean = false,
) {
    val id: Long = idGenerator.getAndIncrement()

    constructor(
        level: NotificationLevel,
        text: String,
        actions: List<NotificationAction> = emptyList(),
        isRead: Boolean = false
    ) : this(
        level = level,
        text = NotificationText.Literal(text),
        args = emptyArray(),
        actions = actions,
        isRead = isRead
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificationData

        if (level != other.level) return false
        if (text != other.text) return false
        if (!args.contentEquals(other.args)) return false
        if (isRead != other.isRead) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = level.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + args.contentHashCode()
        result = 31 * result + isRead.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    companion object {
        private val idGenerator = AtomicLong()
    }
}
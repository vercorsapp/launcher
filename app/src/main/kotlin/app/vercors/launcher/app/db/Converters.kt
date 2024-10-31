package app.vercors.launcher.app.db

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class Converters {
    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }

    @TypeConverter
    fun toDuration(value: Long?): Duration? {
        return value?.milliseconds
    }

    @TypeConverter
    fun fromDuration(duration: Duration?): Long? {
        return duration?.inWholeMilliseconds
    }
}

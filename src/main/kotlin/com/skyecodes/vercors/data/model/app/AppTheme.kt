package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(ThemeSerializer::class)
enum class AppTheme(override val value: String) : StringEnumerable {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark")
}

private class ThemeSerializer : StringEnumerableSerializer<AppTheme>(AppTheme.entries)
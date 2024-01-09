package com.skyecodes.snowball.data.app

import com.skyecodes.snowball.data.StringEnumerable
import com.skyecodes.snowball.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val theme: Theme
) {
    companion object {
        val DEFAULT = Configuration(Theme.SYSTEM)
    }

    @Serializable(with = ThemeSerializer::class)
    enum class Theme(override val value: String) : StringEnumerable {
        SYSTEM("system"),
        LIGHT("light"),
        DARK("dark")
    }

    private class ThemeSerializer : StringEnumerableSerializer<Theme>(Theme.entries)
}
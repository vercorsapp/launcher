package com.skyecodes.vercors.data.app

import com.skyecodes.vercors.data.StringEnumerable
import com.skyecodes.vercors.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val theme: Theme = Theme.SYSTEM,
    val defaultScene: AppScene = AppScene.Home
) {
    companion object {
        val DEFAULT = Configuration()
    }

    @Serializable(with = ThemeSerializer::class)
    enum class Theme(override val value: String) : StringEnumerable {
        SYSTEM("system"),
        LIGHT("light"),
        DARK("dark")
    }

    private class ThemeSerializer : StringEnumerableSerializer<Theme>(Theme.entries)
}
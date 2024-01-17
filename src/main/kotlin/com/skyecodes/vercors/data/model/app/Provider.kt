package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(ProviderSerializer::class)
enum class Provider(override val value: String) : StringEnumerable {
    Modrinth("modrinth"),
    Curseforge("curseforge")
}

private class ProviderSerializer : StringEnumerableSerializer<Provider>(Provider.entries)
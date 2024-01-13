package com.skyecodes.vercors.data.app

import com.skyecodes.vercors.data.StringEnumerable
import com.skyecodes.vercors.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ProviderSerializer::class)
enum class Provider(override val value: String) : StringEnumerable {
    Curseforge("curseforge"),
    Modrinth("modrinth")
}

private class ProviderSerializer : StringEnumerableSerializer<Provider>(Provider.entries)
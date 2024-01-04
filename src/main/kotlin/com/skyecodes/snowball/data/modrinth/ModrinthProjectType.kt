package com.skyecodes.snowball.data.modrinth

import com.skyecodes.snowball.data.StringEnumerable
import com.skyecodes.snowball.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ModrinthProjectTypeSerializer::class)
enum class ModrinthProjectType(override val value: String) : StringEnumerable {
    Mod("mod"),
    Modpack("modpack"),
    ResourcePack("resourcepack"),
    ShaderPack("shader")
}

private class ModrinthProjectTypeSerializer :
    StringEnumerableSerializer<ModrinthProjectType>(ModrinthProjectType.entries)
package com.skyecodes.vercors.data.model.modrinth

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(ModrinthProjectTypeSerializer::class)
enum class ModrinthProjectType(override val value: String) : StringEnumerable {
    Mod("mod"),
    Modpack("modpack"),
    ResourcePack("resourcepack"),
    ShaderPack("shader")
}

private class ModrinthProjectTypeSerializer :
    StringEnumerableSerializer<ModrinthProjectType>(ModrinthProjectType.entries)
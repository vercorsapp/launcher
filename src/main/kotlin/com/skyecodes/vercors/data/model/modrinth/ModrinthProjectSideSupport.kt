package com.skyecodes.vercors.data.model.modrinth

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(ModrinthProjectSideSupportSerializer::class)
enum class ModrinthProjectSideSupport(override val value: String) : StringEnumerable {
    Required("required"),
    Optional("optional"),
    Unsupported("unsupported"),
    Unknown("unknown")
}

private class ModrinthProjectSideSupportSerializer :
    StringEnumerableSerializer<ModrinthProjectSideSupport>(ModrinthProjectSideSupport.entries)
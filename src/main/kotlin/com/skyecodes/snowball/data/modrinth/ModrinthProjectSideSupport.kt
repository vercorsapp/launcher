package com.skyecodes.snowball.data.modrinth

import com.skyecodes.snowball.data.StringEnumerable
import com.skyecodes.snowball.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ModrinthProjectSideSupportSerializer::class)
enum class ModrinthProjectSideSupport(override val value: String) : StringEnumerable {
    Required("required"),
    Optional("optional"),
    Unsupported("unsupported"),
    Unknown("unknown")
}

private class ModrinthProjectSideSupportSerializer :
    StringEnumerableSerializer<ModrinthProjectSideSupport>(ModrinthProjectSideSupport.entries)
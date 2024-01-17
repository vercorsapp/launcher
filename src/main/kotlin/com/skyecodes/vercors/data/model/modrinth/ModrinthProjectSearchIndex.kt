package com.skyecodes.vercors.data.model.modrinth

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(ModrinthProjectSearchIndexSerializer::class)
enum class ModrinthProjectSearchIndex(override val value: String) : StringEnumerable {
    Relevance("relevance"),
    Downloads("downloads"),
    Follows("follows"),
    Newest("newest"),
    Updated("updated")
}

private class ModrinthProjectSearchIndexSerializer :
    StringEnumerableSerializer<ModrinthProjectSearchIndex>(ModrinthProjectSearchIndex.entries)
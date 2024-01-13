package com.skyecodes.vercors.data.modrinth

import com.skyecodes.vercors.data.StringEnumerable

enum class ModrinthProjectSearchIndex(override val value: String) : StringEnumerable {
    Relevance("relevance"),
    Downloads("downloads"),
    Follows("follows"),
    Newest("newest"),
    Updated("updated")
}
package com.skyecodes.snowball.data.modrinth

import com.skyecodes.snowball.data.StringEnumerable

enum class ModrinthProjectSearchIndex(override val value: String) : StringEnumerable {
    Relevance("relevance"),
    Downloads("downloads"),
    Follows("follows"),
    Newest("newest"),
    Updated("updated")
}
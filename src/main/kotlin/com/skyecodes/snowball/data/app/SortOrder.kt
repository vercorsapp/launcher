package com.skyecodes.snowball.data.app

import com.skyecodes.snowball.data.StringEnumerable

enum class SortOrder(override val value: String) : StringEnumerable {
    Asc("asc"), Desc("desc")
}
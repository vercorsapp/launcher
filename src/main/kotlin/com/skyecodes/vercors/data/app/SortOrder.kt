package com.skyecodes.vercors.data.app

import com.skyecodes.vercors.data.StringEnumerable

enum class SortOrder(override val value: String) : StringEnumerable {
    Asc("asc"), Desc("desc")
}
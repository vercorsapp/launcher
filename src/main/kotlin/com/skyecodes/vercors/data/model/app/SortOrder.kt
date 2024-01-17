package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(SortOrderSerializer::class)
enum class SortOrder(override val value: String) : StringEnumerable {
    Asc("asc"), Desc("desc")
}

private class SortOrderSerializer : StringEnumerableSerializer<SortOrder>(SortOrder.entries)
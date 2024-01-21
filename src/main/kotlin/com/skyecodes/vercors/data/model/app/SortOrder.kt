package com.skyecodes.vercors.data.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SortOrder(val value: String) {
    @SerialName("asc")
    Asc("asc"),

    @SerialName("desc")
    Desc("desc")
}

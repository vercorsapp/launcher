package com.skyecodes.vercors.data.model.modrinth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModrinthProjectSearchIndex {
    @SerialName("relevance")
    Relevance,

    @SerialName("downloads")
    Downloads,

    @SerialName("follows")
    Follows,

    @SerialName("newest")
    Newest,

    @SerialName("updated")
    Updated
}

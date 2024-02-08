package com.skyecodes.vercors.projects.modrinth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProjectSearchResult(
    val hits: List<ModrinthProjectResult>,
    val limit: Int,
    val offset: Int,
    @SerialName("total_hits")
    val totalHits: Int
)
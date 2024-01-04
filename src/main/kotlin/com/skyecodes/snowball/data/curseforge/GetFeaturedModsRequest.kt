package com.skyecodes.snowball.data.curseforge


import kotlinx.serialization.Serializable

@Serializable
data class GetFeaturedModsRequest(
    val excludedModIds: List<Int>,
    val gameId: Int,
    val gameVersionTypeId: Int? = null
)
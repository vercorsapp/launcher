package com.skyecodes.vercors.data.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeProjectSearchResponse(
    val data: List<CurseforgeProject>,
    val pagination: CurseforgePagination
)
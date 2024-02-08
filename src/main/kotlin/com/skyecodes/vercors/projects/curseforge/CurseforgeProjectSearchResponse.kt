package com.skyecodes.vercors.projects.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeProjectSearchResponse(
    val data: List<CurseforgeProject>,
    val pagination: CurseforgePagination
)
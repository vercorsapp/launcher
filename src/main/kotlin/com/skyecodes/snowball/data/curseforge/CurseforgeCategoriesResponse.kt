package com.skyecodes.snowball.data.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeCategoriesResponse(
    val data: List<CurseforgeCategory>
)

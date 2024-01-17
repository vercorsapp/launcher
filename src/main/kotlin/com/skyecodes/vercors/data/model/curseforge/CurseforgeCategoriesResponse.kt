package com.skyecodes.vercors.data.model.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeCategoriesResponse(
    val data: List<CurseforgeCategory>
)

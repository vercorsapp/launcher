package com.skyecodes.vercors.projects.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeCategoriesResponse(
    val data: List<CurseforgeCategory>
)

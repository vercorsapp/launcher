package com.skyecodes.snowball.data.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeCategory(
    val classId: Int? = null,
    val dateModified: CurseforgeInstant,
    val displayIndex: Int? = null,
    val gameId: Int,
    val iconUrl: String,
    val id: Int,
    val isClass: Boolean? = null,
    val name: String,
    val parentCategoryId: Int? = null,
    val slug: String,
    val url: String
)
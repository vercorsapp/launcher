package com.skyecodes.snowball.data.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgePagination(
    val index: Int,
    val pageSize: Int,
    val resultCount: Int,
    val totalCount: Int
)
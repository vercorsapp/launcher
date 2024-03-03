package app.vercors.project.curseforge.data

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgePagination(
    val index: Int,
    val pageSize: Int,
    val resultCount: Int,
    val totalCount: Int
)
package app.vercors.project.curseforge.data

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeProjectSearchResponse(
    val data: List<CurseforgeProject>,
    val pagination: CurseforgePagination
)
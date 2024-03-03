package app.vercors.project.curseforge.data

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeCategoriesResponse(
    val data: List<CurseforgeCategory>
)

package app.vercors.project.curseforge

import app.vercors.common.SortOrder
import app.vercors.home.HomeProvider
import app.vercors.project.curseforge.data.CurseforgeCategoriesResponse
import app.vercors.project.curseforge.data.CurseforgeProjectSearchResponse
import app.vercors.project.curseforge.data.CurseforgeProjectSearchSortField

interface CurseforgeService : HomeProvider {
    suspend fun getPopularWorlds(): CurseforgeProjectSearchResponse

    suspend fun getClasses(): CurseforgeCategoriesResponse

    suspend fun search(
        classId: Int,
        searchFilter: String? = null,
        sortField: CurseforgeProjectSearchSortField = CurseforgeProjectSearchSortField.Popularity,
        sortOrder: SortOrder = SortOrder.Desc,
        pageSize: Int = 10
    ): CurseforgeProjectSearchResponse
}

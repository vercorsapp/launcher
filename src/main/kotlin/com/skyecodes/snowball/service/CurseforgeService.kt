package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.app.SortOrder
import com.skyecodes.snowball.data.curseforge.CurseforgeCategoriesResponse
import com.skyecodes.snowball.data.curseforge.CurseforgeProjectSearchResponse
import com.skyecodes.snowball.data.curseforge.CurseforgeProjectSearchSortField

interface CurseforgeService {
    suspend fun getPopularMods(): CurseforgeProjectSearchResponse

    suspend fun getPopularModpacks(): CurseforgeProjectSearchResponse

    suspend fun getPopularResourcePacks(): CurseforgeProjectSearchResponse

    suspend fun getPopularShaderPacks(): CurseforgeProjectSearchResponse

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


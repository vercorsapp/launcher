package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.modrinth.ModrinthProjectSearchIndex
import com.skyecodes.snowball.data.modrinth.ModrinthProjectSearchResult

interface ModrinthService {
    suspend fun getPopularMods(): ModrinthProjectSearchResult

    suspend fun getPopularModpacks(): ModrinthProjectSearchResult

    suspend fun getPopularResourcePacks(): ModrinthProjectSearchResult

    suspend fun getPopularShaderPacks(): ModrinthProjectSearchResult

    suspend fun search(
        facets: List<Map<String, String>> = emptyList(),
        query: String? = null,
        index: ModrinthProjectSearchIndex? = null,
        offset: Int? = null,
        limit: Int? = null
    ): ModrinthProjectSearchResult
}
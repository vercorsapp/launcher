package app.vercors.project.modrinth

import app.vercors.home.HomeProvider
import app.vercors.project.modrinth.data.ModrinthProjectSearchIndex
import app.vercors.project.modrinth.data.ModrinthProjectSearchResult

interface ModrinthService : HomeProvider {
    suspend fun search(
        facets: List<Map<String, String>>? = null,
        query: String? = null,
        index: ModrinthProjectSearchIndex? = null,
        offset: Int? = null,
        limit: Int? = null
    ): ModrinthProjectSearchResult
}
package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.data.model.curseforge.CurseforgeCategoriesResponse
import com.skyecodes.vercors.data.model.curseforge.CurseforgeProjectSearchResponse
import com.skyecodes.vercors.data.model.curseforge.CurseforgeProjectSearchSortField
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope

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
        sortOrder: com.skyecodes.vercors.data.model.app.SortOrder = com.skyecodes.vercors.data.model.app.SortOrder.Desc,
        pageSize: Int = 10
    ): CurseforgeProjectSearchResponse
}

class CurseforgeServiceImpl(
    coroutineScope: CoroutineScope,
    private val httpClient: HttpClient,
    private val apiKey: String
) : CurseforgeService, CoroutineScope by coroutineScope {
    override suspend fun getPopularMods() = search(MOD_CLASS_ID)

    override suspend fun getPopularModpacks() = search(MODPACK_CLASS_ID)

    override suspend fun getPopularResourcePacks() = search(RESOURCEPACK_CLASS_ID)

    override suspend fun getPopularShaderPacks() = search(SHADERPACK_CLASS_ID)

    override suspend fun getPopularWorlds() = search(WORLD_CLASS_ID)

    override suspend fun getClasses(): CurseforgeCategoriesResponse = get("/v1/categories") {
        parameter("gameId", MINECRAFT_GAME_ID)
        parameter("classesOnly", true)
    }.body()

    override suspend fun search(
        classId: Int,
        searchFilter: String?,
        sortField: CurseforgeProjectSearchSortField,
        sortOrder: com.skyecodes.vercors.data.model.app.SortOrder,
        pageSize: Int
    ): CurseforgeProjectSearchResponse = get("/v1/mods/search") {
        parameter("gameId", MINECRAFT_GAME_ID)
        parameter("classId", classId)
        if (searchFilter != null) parameter("searchFilter", searchFilter)
        parameter("sortField", sortField.value)
        parameter("sortOrder", sortOrder.value)
        parameter("pageSize", pageSize)
    }.body()

    private suspend fun get(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.get(BASE_URL + urlString) {
        init()
        block()
    }

    private suspend inline fun <reified T> post(
        urlString: String,
        body: T,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.post(BASE_URL + urlString) {
        init()
        jsonBody(body)
        block()
    }

    private fun HttpRequestBuilder.init() {
        header("x-api-key", apiKey)
    }

    companion object {
        private const val BASE_URL = "https://api.curseforge.com"
        private const val MINECRAFT_GAME_ID = 432
        private const val MODPACK_CLASS_ID = 4471
        private const val MOD_CLASS_ID = 6
        private const val RESOURCEPACK_CLASS_ID = 12
        private const val SHADERPACK_CLASS_ID = 6552
        private const val WORLD_CLASS_ID = 17
    }
}


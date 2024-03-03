package app.vercors.project.curseforge

import app.vercors.common.SortOrder
import app.vercors.jsonBody
import app.vercors.project.curseforge.data.CurseforgeCategoriesResponse
import app.vercors.project.curseforge.data.CurseforgeProjectSearchResponse
import app.vercors.project.curseforge.data.CurseforgeProjectSearchSortField
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class CurseforgeServiceImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val apiUrl: String = "https://api.curseforge.com"
) : CurseforgeService {
    override suspend fun getPopularMods() = search(MOD_CLASS_ID).data.convertAll()

    override suspend fun getPopularModpacks() = search(MODPACK_CLASS_ID).data.convertAll()

    override suspend fun getPopularResourcePacks() = search(RESOURCEPACK_CLASS_ID).data.convertAll()

    override suspend fun getPopularShaderPacks() = search(SHADERPACK_CLASS_ID).data.convertAll()

    override suspend fun getPopularWorlds() = search(WORLD_CLASS_ID)

    override suspend fun getClasses(): CurseforgeCategoriesResponse = get("/v1/categories") {
        parameter("gameId", MINECRAFT_GAME_ID)
        parameter("classesOnly", true)
    }.body()

    override suspend fun search(
        classId: Int,
        searchFilter: String?,
        sortField: CurseforgeProjectSearchSortField,
        sortOrder: SortOrder,
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
    ): HttpResponse = httpClient.get(apiUrl + urlString) {
        init()
        block()
    }

    private suspend inline fun <reified T> post(
        urlString: String,
        body: T,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.post(apiUrl + urlString) {
        init()
        jsonBody(body)
        block()
    }

    private fun HttpRequestBuilder.init() {
        header("x-api-key", apiKey)
    }

    companion object {
        private const val MINECRAFT_GAME_ID = 432
        private const val MODPACK_CLASS_ID = 4471
        private const val MOD_CLASS_ID = 6
        private const val RESOURCEPACK_CLASS_ID = 12
        private const val SHADERPACK_CLASS_ID = 6552
        private const val WORLD_CLASS_ID = 17
    }
}
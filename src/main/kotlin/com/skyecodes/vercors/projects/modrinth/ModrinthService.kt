package com.skyecodes.vercors.projects.modrinth

import com.skyecodes.vercors.jsonBody
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope

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

class ModrinthServiceImpl(
    coroutineScope: CoroutineScope,
    private val httpClient: HttpClient,
    private val apiKey: String
) : ModrinthService, CoroutineScope by coroutineScope {
    override suspend fun getPopularMods() = getPopular("mod")

    override suspend fun getPopularModpacks() = getPopular("modpack")

    override suspend fun getPopularResourcePacks() = getPopular("resourcepack")

    override suspend fun getPopularShaderPacks() = getPopular("shader")

    private suspend fun getPopular(projectType: String) = search(listOf(mapOf("project_type" to projectType)))

    override suspend fun search(
        facets: List<Map<String, String>>,
        query: String?,
        index: ModrinthProjectSearchIndex?,
        offset: Int?,
        limit: Int?
    ): ModrinthProjectSearchResult = get("/search") {
        if (facets.isNotEmpty()) parameter(
            "facets",
            facets.joinToString(", ") { map ->
                map.entries.joinToString(", ") { (key, value) -> "\"$key:$value\"" }.let { "[$it]" }
            }.let { "[$it]" })
        if (query != null) parameter("query", query)
        if (index != null) parameter("index", index)
        if (offset != null) parameter("offset", offset)
        if (limit != null) parameter("limit", limit)
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
        header("Authorization", apiKey)
    }

    companion object {
        private const val BASE_URL = "https://api.modrinth.com/v2"
    }
}
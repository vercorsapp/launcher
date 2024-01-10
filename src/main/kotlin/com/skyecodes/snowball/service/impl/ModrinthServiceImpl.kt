package com.skyecodes.snowball.service.impl

import com.skyecodes.snowball.data.modrinth.ModrinthProjectSearchIndex
import com.skyecodes.snowball.data.modrinth.ModrinthProjectSearchResult
import com.skyecodes.snowball.service.HttpService
import com.skyecodes.snowball.service.ModrinthService
import com.skyecodes.snowball.service.jsonBody
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ModrinthServiceImpl(private val httpService: HttpService) : ModrinthService {
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
    ): HttpResponse = httpService.client.get(BASE_URL + urlString) {
        init()
        block()
    }

    private suspend inline fun <reified T> post(
        urlString: String,
        body: T,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpService.client.post(BASE_URL + urlString) {
        init()
        jsonBody(body)
        block()
    }

    private fun HttpRequestBuilder.init() {
        header("Authorization", API_KEY)
        expectSuccess = true
    }

    companion object {
        private const val BASE_URL = "https://api.modrinth.com/v2"
        private const val API_KEY = "mrp_feID7t7UQm0KumC2AX7oCWHYHFnwJR2sZ64qM0kPNE8TsiJGAFwKpzU6COyo"
    }
}
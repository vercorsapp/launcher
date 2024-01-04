package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.modrinth.ModrinthSearchResult
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object ModrinthApi {
    private const val BASE_URL = "https://api.modrinth.com/v2"
    private const val API_KEY = "mrp_feID7t7UQm0KumC2AX7oCWHYHFnwJR2sZ64qM0kPNE8TsiJGAFwKpzU6COyo"

    suspend fun getPopularMods(): ModrinthSearchResult = getModrinth("/search") {
        parameter("facets", "[[\"project_type:mod\"]]")
        parameter("limit", 10)
    }.body()

    suspend fun getRecentlyUpdatedMods(): ModrinthSearchResult = getModrinth("/search") {
        parameter("facets", "[[\"project_type:mod\"]]")
        parameter("limit", 10)
        parameter("index", "updated")
    }.body()

    private suspend fun getModrinth(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = HttpClient.get(BASE_URL + urlString) {
        init()
        block()
    }

    private suspend inline fun <reified T> postModrinth(
        urlString: String,
        body: T,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = HttpClient.post(BASE_URL + urlString) {
        init()
        jsonBody(body)
        block()
    }

    private fun HttpRequestBuilder.init() {
        initGlobal()
        header("Authorization", API_KEY)
    }
}
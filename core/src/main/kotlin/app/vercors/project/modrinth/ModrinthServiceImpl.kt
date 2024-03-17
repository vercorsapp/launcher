/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.project.modrinth

import app.vercors.common.jsonBody
import app.vercors.project.modrinth.data.ModrinthProjectSearchIndex
import app.vercors.project.modrinth.data.ModrinthProjectSearchResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ModrinthServiceImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val apiUrl: String = "https://api.modrinth.com/v2"
) : ModrinthService {
    override suspend fun getPopularMods() = getPopular("mod").hits.convertAll()

    override suspend fun getPopularModpacks() = getPopular("modpack").hits.convertAll()

    override suspend fun getPopularResourcePacks() = getPopular("resourcepack").hits.convertAll()

    override suspend fun getPopularShaderPacks() = getPopular("shader").hits.convertAll()

    private suspend fun getPopular(projectType: String) = search(listOf(mapOf("project_type" to projectType)))

    override suspend fun search(
        facets: List<Map<String, String>>?,
        query: String?,
        index: ModrinthProjectSearchIndex?,
        offset: Int?,
        limit: Int?
    ): ModrinthProjectSearchResult = get("/search") {
        facets?.let { f ->
            parameter("facets", f.joinToString(",") { map ->
                map.entries.joinToString(",") { (key, value) -> "\"$key:$value\"" }.let { "[$it]" }
            }.let { "[$it]" })
        }
        query?.let { parameter("query", it) }
        index?.let { parameter("index", it) }
        offset?.let { parameter("offset", it) }
        limit?.let { parameter("limit", it) }
    }.body()

    private suspend fun get(
        apiPath: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.get(apiUrl + apiPath) {
        setup()
        block()
    }

    private suspend inline fun <reified T> post(
        apiPath: String,
        body: T,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.post(apiUrl + apiPath) {
        setup()
        jsonBody(body)
        block()
    }

    private fun HttpRequestBuilder.setup() {
        header("Authorization", apiKey)
    }
}
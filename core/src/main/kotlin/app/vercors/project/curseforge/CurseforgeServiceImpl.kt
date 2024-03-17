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

package app.vercors.project.curseforge

import app.vercors.common.SortOrder
import app.vercors.common.jsonBody
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
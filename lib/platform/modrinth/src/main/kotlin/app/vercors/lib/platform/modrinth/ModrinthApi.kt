/*
 * Copyright (c) 2024-2025 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.lib.platform.modrinth

import app.vercors.lib.domain.RemoteResult
import app.vercors.lib.network.RemoteResultConverterFactory
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.*

@Suppress("kotlin:S6517")
interface ModrinthApi {
    @GET("v2/search")
    suspend fun search(
        @Query("facets") facets: String? = null,
        @Query("query") query: String? = null,
        @Query("index") index: ModrinthProjectSearchIndex? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
    ): RemoteResult<ModrinthProjectSearchResult>

    companion object {
        const val BASE_URL = "https://api.modrinth.com/"
    }
}


fun ModrinthApi(
    httpClient: HttpClient,
    baseUrl: String = ModrinthApi.BASE_URL,
): ModrinthApi =
    Ktorfit.Builder()
        .baseUrl(baseUrl)
        .httpClient(httpClient)
        .converterFactories(RemoteResultConverterFactory)
        .build()
        .createModrinthApi()
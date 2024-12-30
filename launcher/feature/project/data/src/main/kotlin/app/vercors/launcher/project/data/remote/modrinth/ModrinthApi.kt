/*
 * Copyright (c) 2024 skyecodes
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

package app.vercors.launcher.project.data.remote.modrinth

import app.vercors.launcher.project.data.remote.modrinth.dto.ModrinthProjectSearchResult
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

interface ModrinthApi {
    @GET("v2/search")
    fun search(
        @Query query: String? = null,
        @Query facets: String? = null,
        @Query index: String? = null,
        @Query offset: Int? = null,
        @Query limit: Int? = null
    ): Flow<ModrinthProjectSearchResult>
}

@Single
fun provideModrinthApi(httpClient: HttpClient): ModrinthApi {
    return Ktorfit.Builder()
        .httpClient(httpClient)
        .baseUrl("https://api.modrinth.com/")
        .converterFactories(FlowConverterFactory())
        .build()
        .createModrinthApi()
}
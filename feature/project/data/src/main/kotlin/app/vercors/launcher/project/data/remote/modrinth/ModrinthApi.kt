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
package app.vercors.launcher.project.data.remote.curseforge

import app.vercors.launcher.project.data.remote.curseforge.dto.CurseforgeProjectSearchResponse
import app.vercors.launcher.project.data.remote.curseforge.dto.CurseforgeProjectSearchSortField
import app.vercors.launcher.project.data.remote.curseforge.dto.CurseforgeProjectSearchSortOrder
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single

interface CurseforgeApi {
    @GET("v1/mods/search")
    fun search(
        @Query gameId: Int,
        @Query classId: Int? = null,
        @Query categoryId: Int? = null,
        @Query sortField: Int? = CurseforgeProjectSearchSortField.Popularity.value,
        @Query sortOrder: String? = CurseforgeProjectSearchSortOrder.Desc.value,
        @Query index: Int? = null,
        @Query pageSize: Int? = null
    ): Flow<CurseforgeProjectSearchResponse>
}

@Single
fun provideCurseforgeApi(
    httpClient: HttpClient,
    @Property("curseforgeApiKey") apiKey: String
): CurseforgeApi {
    return Ktorfit.Builder()
        .httpClient(httpClient.config {
            defaultRequest {
                header("x-api-key", apiKey)
            }
        })
        .baseUrl("https://api.curseforge.com/")
        .converterFactories(FlowConverterFactory())
        .build()
        .createCurseforgeApi()
}
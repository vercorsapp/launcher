package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.curseforge.GetFeaturedModsReponse
import com.skyecodes.snowball.data.curseforge.GetFeaturedModsRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object CurseforgeApi {
    private const val BASE_URL = "https://api.curseforge.com"
    private const val API_KEY = "\$2a\$10\$lCr8PZ1p3YwsVpHK4euhJe..bDmLaj52azYRTfvP.oOZghtCD.hbi"
    private const val MINECRAFT_GAME_ID = 432

    suspend fun getFeaturedMods(excludedModIds: List<Int>): GetFeaturedModsReponse =
        postCurseforge("/v1/mods/featured", GetFeaturedModsRequest(excludedModIds, MINECRAFT_GAME_ID)).body()

    private suspend fun getCurseforge(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = HttpClient.get(BASE_URL + urlString) {
        init()
        block()
    }

    private suspend inline fun <reified T> postCurseforge(
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
        header("x-api-key", API_KEY)
    }
}
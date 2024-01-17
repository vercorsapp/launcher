package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.data.model.app.Project
import com.skyecodes.vercors.data.model.app.Provider
import com.skyecodes.vercors.data.model.app.convertCurseforge
import com.skyecodes.vercors.data.model.app.convertModrinth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

interface HomeProviderService {
    suspend fun getPopularMods(scope: CoroutineScope, providers: List<Provider>): List<Project>
    suspend fun getPopularModpacks(scope: CoroutineScope, providers: List<Provider>): List<Project>
    suspend fun getPopularResourcePacks(scope: CoroutineScope, providers: List<Provider>): List<Project>
    suspend fun getPopularShaderPacks(scope: CoroutineScope, providers: List<Provider>): List<Project>
}

class HomeProviderServiceImpl(
    private val modrinthService: ModrinthService,
    private val curseforgeService: CurseforgeService
) : HomeProviderService {
    private val popularModsProviders: Map<Provider, suspend () -> List<Project>> = mapOf(
        Provider.Modrinth to { modrinthService.getPopularMods().hits.convertModrinth() },
        Provider.Curseforge to { curseforgeService.getPopularMods().data.convertCurseforge() }
    )
    private val popularModpacksProviders: Map<Provider, suspend () -> List<Project>> = mapOf(
        Provider.Modrinth to { modrinthService.getPopularModpacks().hits.convertModrinth() },
        Provider.Curseforge to { curseforgeService.getPopularModpacks().data.convertCurseforge() }
    )
    private val popularResourcePacksProviders: Map<Provider, suspend () -> List<Project>> = mapOf(
        Provider.Modrinth to { modrinthService.getPopularResourcePacks().hits.convertModrinth() },
        Provider.Curseforge to { curseforgeService.getPopularResourcePacks().data.convertCurseforge() }
    )
    private val popularShaderPacksProviders: Map<Provider, suspend () -> List<Project>> = mapOf(
        Provider.Modrinth to { modrinthService.getPopularShaderPacks().hits.convertModrinth() },
        Provider.Curseforge to { curseforgeService.getPopularShaderPacks().data.convertCurseforge() }
    )

    override suspend fun getPopularMods(scope: CoroutineScope, providers: List<Provider>): List<Project> =
        merge(providers.map { scope.async { popularModsProviders[it]!!() } })

    override suspend fun getPopularModpacks(scope: CoroutineScope, providers: List<Provider>): List<Project> =
        merge(providers.map { scope.async { popularModpacksProviders[it]!!() } })

    override suspend fun getPopularResourcePacks(scope: CoroutineScope, providers: List<Provider>): List<Project> =
        merge(providers.map { scope.async { popularResourcePacksProviders[it]!!() } })

    override suspend fun getPopularShaderPacks(scope: CoroutineScope, providers: List<Provider>): List<Project> =
        merge(providers.map { scope.async { popularShaderPacksProviders[it]!!() } })

    private suspend fun merge(asyncs: List<Deferred<List<Project>>>): List<Project> =
        asyncs.awaitAll().map { it.toMutableList() }.filter { it.isNotEmpty() }.let { lists ->
            buildList {
                if (lists.isNotEmpty()) {
                    var curListIdx = 0
                    while (size < 10) {
                        val v = lists[curListIdx].removeFirst()
                        if (none { it.name == v.name }) {
                            add(v)
                            curListIdx++
                            if (curListIdx == lists.size) curListIdx = 0
                        }
                    }
                }
            }
        }
}
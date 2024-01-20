package com.skyecodes.vercors.component.screen

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.*
import com.skyecodes.vercors.data.service.CurseforgeService
import com.skyecodes.vercors.data.service.ModrinthService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface HomeComponent : Refreshable {
    val uiState: StateFlow<HomeUiState>
}

class DefaultHomeComponent(
    componentContext: AppComponentContext,
    private val configuration: StateFlow<Configuration?>,
    private val instances: StateFlow<List<Instance>?>,
    private val modrinthService: ModrinthService = componentContext.get(),
    private val curseforgeService: CurseforgeService = componentContext.get()
) : AppComponentContext by componentContext, HomeComponent {
    companion object {
        private val cachedProjectsData: MutableMap<Pair<HomeSectionType, Provider>, List<Project>> = mutableMapOf()
    }

    override val uiState = MutableStateFlow(HomeUiState(emptyMap()))

    init {
        lifecycle.doOnCreate { initialize() }
    }

    private fun initialize() {
        scope.launch {
            instances.filterNotNull().collect { updateInstances(it) }
        }
        scope.launch {
            configuration.filterNotNull().collect { updateAll(it) }
        }
    }

    private fun updateInstances(instances: List<Instance>) {
        uiState.update { state ->
            state.copy(
                sections = state.sections.mapValues { (type, section) ->
                    if (type === HomeSectionType.JumpBackIn) HomeUiState.Section.Instances(instances)
                    else section
                }
            )
        }
    }

    override fun refresh() {
        cachedProjectsData.clear()
        updateAll(configuration.value!!)
    }

    private fun updateAll(configuration: Configuration) {
        uiState.update { HomeUiState(configuration.homeSections.associateWith { emptySectionData(it) }) }
        configuration.homeSections.forEach { type ->
            scope.launch {
                val section = getSectionData(type, configuration.homeProviders)
                uiState.update {
                    HomeUiState(it.sections.mapValues { (mapType, mapSection) ->
                        if (mapType === type) section else mapSection
                    })
                }
            }
        }
    }

    private fun emptySectionData(sectionType: HomeSectionType) = when (sectionType) {
        HomeSectionType.JumpBackIn -> HomeUiState.Section.Instances(null)
        else -> HomeUiState.Section.Projects(null)
    }

    private suspend fun getSectionData(sectionType: HomeSectionType, providers: List<Provider>): HomeUiState.Section =
        if (sectionType === HomeSectionType.JumpBackIn) HomeUiState.Section.Instances(instances.value?.sortedByDescending {
            it.lastLaunched ?: it.created
        })
        else HomeUiState.Section.Projects(providers.map { scope.async { getProjectsData(sectionType, it) } }
            .awaitAll().map { it.toMutableList() }.filter { it.isNotEmpty() }.let { lists ->
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
            })

    private suspend fun getProjectsData(sectionType: HomeSectionType, provider: Provider): List<Project> =
        cachedProjectsData.getOrPut(sectionType to provider) { fetchProjectsData(sectionType, provider) }

    private suspend fun fetchProjectsData(sectionType: HomeSectionType, provider: Provider): List<Project> =
        when (sectionType) {
            HomeSectionType.PopularMods -> when (provider) {
                Provider.Modrinth -> modrinthService.getPopularMods().hits.convertModrinth()
                Provider.Curseforge -> curseforgeService.getPopularMods().data.convertCurseforge()
            }

            HomeSectionType.PopularModpacks -> when (provider) {
                Provider.Modrinth -> modrinthService.getPopularModpacks().hits.convertModrinth()
                Provider.Curseforge -> curseforgeService.getPopularModpacks().data.convertCurseforge()
            }

            HomeSectionType.PopularResourcePacks -> when (provider) {
                Provider.Modrinth -> modrinthService.getPopularResourcePacks().hits.convertModrinth()
                Provider.Curseforge -> curseforgeService.getPopularResourcePacks().data.convertCurseforge()
            }

            HomeSectionType.PopularShaderPacks -> when (provider) {
                Provider.Modrinth -> modrinthService.getPopularShaderPacks().hits.convertModrinth()
                Provider.Curseforge -> curseforgeService.getPopularShaderPacks().data.convertCurseforge()
            }

            else -> emptyList()
    }
}

data class HomeUiState(val sections: Map<HomeSectionType, Section>) {
    sealed interface Section {
        data class Projects(val projects: List<Project>?) : Section
        data class Instances(val instances: List<Instance>?) : Section
    }
}
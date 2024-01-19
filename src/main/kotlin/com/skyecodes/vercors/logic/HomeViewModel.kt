package com.skyecodes.vercors.logic

import com.skyecodes.vercors.data.model.app.*
import com.skyecodes.vercors.data.service.CurseforgeService
import com.skyecodes.vercors.data.service.ModrinthService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel(
    private val modrinthService: ModrinthService,
    private val curseforgeService: CurseforgeService,
    private val configuration: StateFlow<Configuration?>,
    private val instances: StateFlow<List<Instance>?>,
    savedStateHolder: SavedStateHolder
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(emptyMap()))
    val uiState = _uiState.asStateFlow()

    @Suppress("unchecked_cast")
    private val cachedProjectsData: MutableMap<Pair<HomeSectionType, Provider>, List<Project>> =
        savedStateHolder.consumeRestored("cachedProjectsData") as MutableMap<Pair<HomeSectionType, Provider>, List<Project>>?
            ?: mutableMapOf()

    init {
        savedStateHolder.registerProvider("cachedProjectsData") { cachedProjectsData }
    }

    fun initialize() {
        viewModelScope.launch {
            instances.filterNotNull().collect { updateInstances(it) }
        }
        viewModelScope.launch {
            configuration.filterNotNull().collect { updateAll(it) }
        }
    }

    private fun updateInstances(instances: List<Instance>) {
        _uiState.update { state ->
            state.copy(
                sections = state.sections.mapValues { (type, section) ->
                    if (type === HomeSectionType.JumpBackIn) HomeUiState.Section.Instances(instances)
                    else section
                }
            )
        }
    }

    private fun updateAll(configuration: Configuration) {
        _uiState.update { HomeUiState(configuration.homeSections.associateWith { emptySectionData(it) }) }
        configuration.homeSections.forEach { type ->
            viewModelScope.launch {
                val section = getSectionData(type, configuration.homeProviders)
                _uiState.update {
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
        if (sectionType === HomeSectionType.JumpBackIn) HomeUiState.Section.Instances(instances.value!!.sortedByDescending {
            it.lastLaunched ?: it.created
        })
        else HomeUiState.Section.Projects(providers.map { viewModelScope.async { getProjectsData(sectionType, it) } }
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
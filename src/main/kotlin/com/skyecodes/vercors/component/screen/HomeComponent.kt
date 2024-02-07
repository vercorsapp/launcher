package com.skyecodes.vercors.component.screen

import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.*
import com.skyecodes.vercors.data.service.ConfigurationService
import com.skyecodes.vercors.data.service.CurseforgeService
import com.skyecodes.vercors.data.service.InstanceService
import com.skyecodes.vercors.data.service.ModrinthService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

interface HomeComponent : Refreshable {
    val uiState: StateFlow<UiState>
    val showInstanceDetails: (Instance) -> Unit
    val launchInstance: (Instance) -> Unit
    val showProjectDetails: (Project) -> Unit
    val installProject: (Project) -> Unit

    data class UiState(val sections: Map<HomeSectionType, Section>) {
        sealed interface Section {
            data class Projects(val projects: List<Project>?) : Section
            data class Instances(val instances: List<Instance>?) : Section
        }
    }
}

class DefaultHomeComponent(
    componentContext: AppComponentContext,
    override val showInstanceDetails: (Instance) -> Unit,
    override val launchInstance: (Instance) -> Unit,
    override val showProjectDetails: (Project) -> Unit,
    override val installProject: (Project) -> Unit,
    private val configurationService: ConfigurationService = componentContext.get(),
    private val instanceService: InstanceService = componentContext.get(),
    private val modrinthService: ModrinthService = componentContext.get(),
    private val curseforgeService: CurseforgeService = componentContext.get()
) : AbstractComponent(componentContext), HomeComponent {
    companion object {
        private val cachedProjectsData: MutableMap<Pair<HomeSectionType, Provider>, List<Project>> = mutableMapOf()
    }

    override val uiState = MutableStateFlow(HomeComponent.UiState(emptyMap()))
    private lateinit var configuration: StateFlow<Configuration>
    private lateinit var configurationListener: Job
    private lateinit var instances: StateFlow<List<Instance>>
    private lateinit var instancesListener: Job

    init {
        lifecycle.doOnStart { onStart() }
        lifecycle.doOnStop { onStop() }
    }

    private fun onStart() {
        instancesListener = scope.launch {
            instances = instanceService.instances.stateIn(this)
            instances.collect { updateInstances(it) }
        }
        configurationListener = scope.launch {
            configuration = configurationService.config.stateIn(this)
            configuration.collect { updateAll(it) }
        }
    }

    private fun onStop() {
        instancesListener.cancel()
        configurationListener.cancel()
    }

    private fun updateInstances(instances: List<Instance>) {
        uiState.update { state ->
            state.copy(
                sections = state.sections.mapValues { (type, section) ->
                    if (type === HomeSectionType.JumpBackIn) createInstancesSection(instances)
                    else section
                }
            )
        }
    }

    override fun refresh() {
        cachedProjectsData.clear()
        updateAll(configuration.value)
    }

    private fun updateAll(configuration: Configuration) {
        uiState.update { HomeComponent.UiState(configuration.homeSections.associateWith { emptySectionData(it) }) }
        configuration.homeSections.forEach { type ->
            scope.launch {
                val section = getSectionData(type, configuration.homeProviders)
                uiState.update {
                    HomeComponent.UiState(it.sections.mapValues { (mapType, mapSection) ->
                        if (mapType === type) section else mapSection
                    })
                }
            }
        }
    }

    private fun emptySectionData(sectionType: HomeSectionType) = when (sectionType) {
        HomeSectionType.JumpBackIn -> HomeComponent.UiState.Section.Instances(null)
        else -> HomeComponent.UiState.Section.Projects(null)
    }

    private fun createInstancesSection(instances: List<Instance>) = HomeComponent.UiState.Section.Instances(
        instances.sortedByDescending { it.lastPlayed ?: it.dateCreated }
    )

    private suspend fun getSectionData(
        sectionType: HomeSectionType,
        providers: List<Provider>
    ): HomeComponent.UiState.Section =
        if (sectionType === HomeSectionType.JumpBackIn) createInstancesSection(instances.value)
        else HomeComponent.UiState.Section.Projects(providers.map {
            scope.async(Dispatchers.IO) {
                getProjectsData(
                    sectionType,
                    it
                )
            }
        }
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
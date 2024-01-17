package com.skyecodes.vercors.logic

import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.HomeSectionType
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.model.app.Project
import com.skyecodes.vercors.data.service.HomeProviderService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

private val logger = KotlinLogging.logger {}

class HomeViewModel(
    private val homeProviderService: HomeProviderService,
    private val configuration: StateFlow<Configuration?>,
    private val instances: StateFlow<List<Instance>?>
) : ViewModel() {
    private var isInitialized = false
    private val _uiState = MutableStateFlow(HomeUiState(emptyMap()))
    val uiState = _uiState.asStateFlow()
    private val cachedSectionData = mutableMapOf<HomeSectionType, HomeUiState.Section>()

    fun initialize() {
        if (isInitialized) return
        isInitialized = true
        viewModelScope.launch {
            instances.filterNotNull().collect { updateInstances(it) }
        }
        invalidateCacheOnChange { it?.homeProviders }
        invalidateCacheOnChange { it?.homeSections }
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
                val section = cachedSectionData.getOrPut(type) { fetchSectionData(type, configuration) }
                _uiState.update {
                    HomeUiState(it.sections.mapValues { (mapType, mapSection) ->
                        if (mapType === type) section else mapSection
                    })
                }
            }
        }
    }

    private fun <T> invalidateCacheOnChange(transform: (Configuration?) -> T) {
        viewModelScope.launch {
            configuration.map(transform).collect {
                cachedSectionData.clear()
                updateAll(configuration.value!!)
            }
        }
    }

    private fun emptySectionData(sectionType: HomeSectionType) = when (sectionType) {
        HomeSectionType.JumpBackIn -> HomeUiState.Section.Instances(null)
        else -> HomeUiState.Section.Projects(null)
    }

    private suspend fun fetchSectionData(
        sectionType: HomeSectionType,
        configuration: Configuration
    ): HomeUiState.Section = when (sectionType) {
        HomeSectionType.JumpBackIn -> HomeUiState.Section.Instances(instances.value!!.sortedByDescending {
            it.lastLaunched ?: it.created
        })

        HomeSectionType.PopularMods -> HomeUiState.Section.Projects(
            homeProviderService.getPopularMods(
                viewModelScope,
                configuration.homeProviders
            )
        )

        HomeSectionType.PopularModpacks -> HomeUiState.Section.Projects(
            homeProviderService.getPopularModpacks(
                viewModelScope,
                configuration.homeProviders
            )
        )

        HomeSectionType.PopularResourcePacks -> HomeUiState.Section.Projects(
            homeProviderService.getPopularResourcePacks(
                viewModelScope,
                configuration.homeProviders
            )
        )

        HomeSectionType.PopularShaderPacks -> HomeUiState.Section.Projects(
            homeProviderService.getPopularShaderPacks(
                viewModelScope,
                configuration.homeProviders
            )
        )
    }
}

data class HomeUiState(val sections: Map<HomeSectionType, Section>) {
    sealed interface Section {
        data class Projects(val projects: List<Project>?) : Section
        data class Instances(val instances: List<Instance>?) : Section
    }
}
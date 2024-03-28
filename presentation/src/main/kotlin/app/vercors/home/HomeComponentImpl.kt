/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.home

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.Resource
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationRepository
import app.vercors.instance.Instance
import app.vercors.instance.InstanceRepository
import app.vercors.instance.LaunchInstanceUseCase
import app.vercors.instance.StopInstanceUseCase
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationManager
import app.vercors.project.Project
import app.vercors.project.ProjectProviderType
import app.vercors.project.ProjectRepository
import app.vercors.project.curseforge.CurseforgeProjectRepository
import app.vercors.project.modrinth.ModrinthProjectRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.ConcurrentHashMap

internal class HomeComponentImpl(
    componentContext: AppComponentContext,
    configurationRepository: ConfigurationRepository = componentContext.inject(),
    private val instanceRepository: InstanceRepository = componentContext.inject(),
    private val launchInstanceUseCase: LaunchInstanceUseCase = componentContext.inject(),
    private val stopInstanceUseCase: StopInstanceUseCase = componentContext.inject(),
    private val modrinthProjectRepository: ModrinthProjectRepository = componentContext.inject(),
    private val curseforgeProjectRepository: CurseforgeProjectRepository = componentContext.inject(),
    private val navigationManager: NavigationManager = componentContext.inject(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AbstractAppComponent(componentContext, KotlinLogging.logger {}), HomeComponent {
    private val configState =
        configurationRepository.state.filterNotNull().map { it.homeSections to it.homeProviders }
            .stateIn(localScope, SharingStarted.Eagerly, null)
    private val _state = MutableStateFlow(HomeState())
    override val state: StateFlow<HomeState> = _state

    init {
        configState.filterNotNull().collectInLifecycle { (sectionTypes, providerTypes) ->
            loadSections(sectionTypes, providerTypes)
        }
        instanceRepository.state.collectInLifecycle {
            _state.update { state ->
                HomeState(state.sections.map {
                    if (it.type == HomeSectionType.JumpBackIn) loadInstancesHomeSection() else it
                })
            }
        }
    }

    override fun onIntent(intent: HomeIntent) = when (intent) {
        is HomeIntent.ShowInstanceDetails -> onShowInstanceDetails(intent.instance)
        is HomeIntent.LaunchInstance -> onLaunchInstance(intent.instance)
        is HomeIntent.StopInstance -> onStopInstance(intent.instance)
        is HomeIntent.ShowProjectDetails -> onShowProjectDetails(intent.project)
        is HomeIntent.InstallProject -> onInstallProject(intent.project)
    }

    private fun emptySections(sectionTypes: List<HomeSectionType>): List<HomeSection> {
        return sectionTypes.map {
            when (it) {
                HomeSectionType.JumpBackIn -> HomeSection.Instances()
                else -> HomeSection.Projects(it)
            }
        }
    }

    private suspend fun loadSections(
        sectionTypes: List<HomeSectionType>, providerTypes: List<ProjectProviderType>
    ) = coroutineScope {
        _state.update { HomeState(emptySections(sectionTypes)) }
        sectionTypes.map { sectionType ->
            launch(ioDispatcher) {
                val section = when (sectionType) {
                    HomeSectionType.JumpBackIn -> loadInstancesHomeSection()
                    else -> providerTypes.map { providerType -> getProjectData(sectionType, providerType) }.merge()
                }
                _state.update { state ->
                    HomeState(state.sections.map { if (it.type === sectionType) section else it })
                }
            }
        }.joinAll()
    }

    private suspend fun getProjectData(sectionType: HomeSectionType, providerType: ProjectProviderType) =
        cachedProjectData.getOrPut(sectionType to providerType) {
            loadProjectsHomeSection(sectionType, providerType)
        }

    private suspend fun loadInstancesHomeSection() = HomeSection.Instances(
        instanceRepository.loadingState
            .filterIsInstance<Resource.Loaded<List<Instance>>>().first()
            .result.sortedByDescending { it.data.lastPlayed ?: it.data.dateCreated }
    )

    private suspend fun loadProjectsHomeSection(
        sectionType: HomeSectionType,
        provider: ProjectProviderType
    ): HomeSection.Projects = HomeSection.Projects(
        sectionType, when (sectionType) {
            HomeSectionType.PopularMods -> fetchProjects(provider, ProjectRepository::getPopularMods)
            HomeSectionType.PopularModpacks -> fetchProjects(provider, ProjectRepository::getPopularModpacks)
            HomeSectionType.PopularResourcePacks -> fetchProjects(provider, ProjectRepository::getPopularResourcePacks)
            HomeSectionType.PopularShaderPacks -> fetchProjects(provider, ProjectRepository::getPopularShaderPacks)
            else -> throw IllegalArgumentException("Section type $sectionType not supported by this method")
        }
    )

    private suspend fun fetchProjects(
        provider: ProjectProviderType,
        func: suspend (ProjectRepository) -> List<Project>
    ): List<Project> = func(
        when (provider) {
            ProjectProviderType.Modrinth -> modrinthProjectRepository
            ProjectProviderType.Curseforge -> curseforgeProjectRepository
        }
    )

    override fun refresh() {
        cachedProjectData.clear()
        val (sectionType, providerType) = configState.value!!
        localScope.launch { loadSections(sectionType, providerType) }
    }

    private fun List<HomeSection.Projects>.merge(): HomeSection.Projects {
        return first()
    }

    private fun onShowInstanceDetails(instance: Instance) {
        navigationManager.handle(NavigationEvent.InstanceDetails(instance))
    }

    private fun onLaunchInstance(instance: Instance) {
        localScope.launch { launchInstanceUseCase(instance) }
    }

    private fun onStopInstance(instance: Instance) {
        localScope.launch { stopInstanceUseCase(instance) }
    }

    private fun onShowProjectDetails(project: Project) {
        navigationManager.handle(NavigationEvent.ProjectDetails(project))
    }

    private fun onInstallProject(project: Project) {
        // TODO
    }

    companion object {
        private val cachedProjectData =
            ConcurrentHashMap<Pair<HomeSectionType, ProjectProviderType>, HomeSection.Projects>()
    }
}

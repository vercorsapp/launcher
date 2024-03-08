package app.vercors.home

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationService
import app.vercors.instance.InstanceData
import app.vercors.instance.InstanceService
import app.vercors.instance.launch.LauncherService
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationService
import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger { }

class HomeComponentImpl(
    componentContext: AppComponentContext,
    private val homeService: HomeService = componentContext.inject(),
    private val configurationService: ConfigurationService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject(),
    private val launcherService: LauncherService = componentContext.inject(),
    private val navigationService: NavigationService = componentContext.inject()
) : AbstractAppComponent(componentContext), HomeComponent {

    private val configState = configurationService.configState
        .filterNotNull()
        .map { it.homeSections to it.homeProviders }
        .stateIn(this, SharingStarted.Eagerly, null)
    private val _uiState = MutableStateFlow(HomeUiState())
    override val uiState: StateFlow<HomeUiState> = _uiState
    private lateinit var job: Job

    override fun onStart() {
        super.onStart()
        job = launch {
            configState.filterNotNull()
                .combine(instanceService.instancesState) { c, _ -> c }
                .collect { (sectionTypes, providerTypes) ->
                    loadSections(sectionTypes, providerTypes)
                }
        }
    }

    private fun emptySections(sectionTypes: List<HomeSectionType>): List<HomeSection> {
        return sectionTypes.map {
            when (it) {
                HomeSectionType.JumpBackIn -> HomeSection.Instances()
                else -> HomeSection.Projects(it)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    private suspend fun loadSections(
        sectionTypes: List<HomeSectionType>,
        providerTypes: List<ProjectProviderType>
    ) = coroutineScope {
        logger.info { "Loading home sections" }
        _uiState.update { HomeUiState(emptySections(sectionTypes)) }
        sectionTypes.forEach { sectionType ->
            launch {
                val section = when (sectionType) {
                    HomeSectionType.JumpBackIn -> homeService.loadInstances()
                    else -> providerTypes.map { providerType -> getProjectData(sectionType, providerType) }.merge()
                }
                _uiState.update { state ->
                    state.copy(sections = state.sections.map {
                        if (it.type === sectionType) section else it
                    })
                }
            }
        }
    }

    private suspend fun getProjectData(sectionType: HomeSectionType, providerType: ProjectProviderType) =
        cachedProjectData.getOrPut(sectionType to providerType) { homeService.loadProjects(sectionType, providerType) }

    override fun refresh() {
        cachedProjectData.clear()
        val (sectionType, providerType) = configState.value!!
        launch {
            loadSections(sectionType, providerType)
        }
    }

    private fun List<HomeSection.Projects>.merge(): HomeSection.Projects {
        return first()
    }

    override fun onShowInstanceDetails(instance: InstanceData) {
        navigationService.handle(NavigationEvent.InstanceDetails(instance))
    }

    override fun onLaunchInstance(instance: InstanceData) {
        launch { launcherService.launch(instance).collect() }
    }

    override fun onShowProjectDetails(project: ProjectData) {
        navigationService.handle(NavigationEvent.ProjectDetails(project))
    }

    override fun onInstallProject(project: ProjectData) {
        // TODO
    }

    companion object {
        private val cachedProjectData =
            ConcurrentHashMap<Pair<HomeSectionType, ProjectProviderType>, HomeSection.Projects>()
    }
}

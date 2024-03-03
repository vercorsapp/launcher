package app.vercors.navigation

import app.vercors.common.*
import app.vercors.configuration.ConfigurationComponent
import app.vercors.configuration.ConfigurationService
import app.vercors.dialog.DialogEvent
import app.vercors.home.HomeComponent
import app.vercors.instance.InstanceData
import app.vercors.instance.InstanceListComponent
import app.vercors.instance.details.InstanceDetailsComponent
import app.vercors.project.ProjectData
import app.vercors.project.ProjectDetailsComponent
import app.vercors.project.SearchComponent
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.children.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.subscribe
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import java.util.concurrent.atomic.AtomicLong

private val logger = KotlinLogging.logger { }

class NavigationComponentImpl(
    componentContext: AppComponentContext,
    private val onNavigationChange: (NavigationChildren<*, NavigationChildComponent>) -> Unit,
    private val onLaunchInstance: (InstanceData) -> Unit,
    private val onInstallProject: (ProjectData) -> Unit,
    private val onOpenDialog: (DialogEvent) -> Unit,
    configurationService: ConfigurationService = componentContext.inject(),
    private val navigationService: NavigationService = componentContext.inject()
) : AbstractAppComponent(componentContext), NavigationComponent {
    private val screenId = AtomicLong(0)
    private val nextScreenId get() = screenId.getAndIncrement()
    private val navigation = SimpleNavigation<(NavigationState) -> NavigationState>()
    private val _children: Value<NavigationChildren<NavigationConfig, NavigationChildComponent>> = children(
        source = navigation,
        stateSerializer = NavigationState.serializer(),
        key = "navigation",
        initialState = {
            NavigationState(
                configurations = listOf(defaultConfiguration(configurationService.config.defaultTab)),
                index = 0
            )
        },
        navTransformer = { state, transformer -> transformer(state) },
        stateMapper = { state, children ->
            NavigationChildren(
                items = children.map { it as Child.Created },
                index = state.index
            )
        },
        childFactory = { configuration, componentContext ->
            createChild(configuration, AppComponentContextImpl(componentContext, di))
        }
    )
    override val children: Value<NavigationChildren<*, NavigationChildComponent>> = _children
    private val activeChild get() = _children.value.active.instance
    private val activeConfig get() = _children.value.active.configuration

    override fun onCreate() {
        super.onCreate()
        _children.subscribe(lifecycle) {
            onNavigationChange(it)
            navigationService.onNavigate(it)
        }
    }

    private fun defaultConfiguration(defaultTab: AppTab): NavigationConfig {
        return when (defaultTab) {
            AppTab.Home -> NavigationConfig.Home(nextScreenId)
            AppTab.Instances -> NavigationConfig.InstanceList(nextScreenId)
            AppTab.Search -> NavigationConfig.Search(nextScreenId)
            AppTab.Settings -> NavigationConfig.Configuration(nextScreenId)
        }
    }

    private fun createChild(config: NavigationConfig, componentContext: AppComponentContext): NavigationChildComponent =
        when (config) {
            is NavigationConfig.Home -> inject<HomeComponent>(
                componentContext,
                ::showInstanceDetails,
                onLaunchInstance,
                ::showProjectDetails,
                onInstallProject
            )

            is NavigationConfig.InstanceList -> inject<InstanceListComponent>(
                componentContext,
                ::showInstanceDetails,
                onLaunchInstance,
                ::onOpenCreateInstanceDialog
            )

            is NavigationConfig.Search -> inject<SearchComponent>(componentContext)
            is NavigationConfig.Configuration -> inject<ConfigurationComponent>(componentContext)
            is NavigationConfig.InstanceDetails -> inject<InstanceDetailsComponent>(componentContext)
            is NavigationConfig.ProjectDetails -> inject<ProjectDetailsComponent>(componentContext)
        }

    private fun showInstanceDetails(instance: InstanceData) = navigate(NavigationEvent.InstanceDetails(instance))

    private fun showProjectDetails(project: ProjectData) = navigate(NavigationEvent.ProjectDetails(project))

    private fun onOpenCreateInstanceDialog() = onOpenDialog(DialogEvent.CreateInstance)

    override fun navigate(event: NavigationEvent) {
        when (event) {
            NavigationEvent.Previous -> {
                if (children.value.index > 0) {
                    logger.info { "Navigating to previous screen..." }
                    navigation.navigate { it.copy(index = it.index - 1) }
                }
            }

            NavigationEvent.Next -> {
                if (children.value.index < children.value.items.size - 1) {
                    logger.info { "Navigating to next screen..." }
                    navigation.navigate { it.copy(index = it.index + 1) }
                }
            }

            NavigationEvent.Refresh -> {
                val current = activeChild
                if (current is Refreshable) {
                    logger.info { "Refreshing screen" }
                    current.refresh()
                }
            }

            NavigationEvent.Home -> doNavigate(NavigationConfig.Home(nextScreenId))
            NavigationEvent.InstanceList -> doNavigate(NavigationConfig.InstanceList(nextScreenId))
            NavigationEvent.Search -> doNavigate(NavigationConfig.Search(nextScreenId))
            NavigationEvent.Configuration -> doNavigate(NavigationConfig.Configuration(nextScreenId))
            is NavigationEvent.InstanceDetails -> doNavigate(
                NavigationConfig.InstanceDetails(
                    nextScreenId,
                    event.instance
                )
            )

            is NavigationEvent.ProjectDetails -> doNavigate(
                NavigationConfig.ProjectDetails(
                    nextScreenId,
                    event.project
                )
            )
        }
    }

    private fun doNavigate(config: NavigationConfig) {
        if (!config.equalsIgnoreScreenId(activeConfig)) {
            logger.info { "Navigating to ${config.javaClass.simpleName}" }
            navigation.navigate {
                it.copy(
                    configurations = it.configurations.filterIndexed { i, _ -> i <= it.index } + config,
                    index = it.index + 1
                )
            }
        }
    }

    @Serializable
    private sealed interface NavigationConfig {
        fun equalsIgnoreScreenId(other: NavigationConfig): Boolean

        @Serializable
        data class Home(val screenId: Long) : NavigationConfig {
            override fun equalsIgnoreScreenId(other: NavigationConfig) = other is Home
        }

        @Serializable
        data class InstanceList(val screenId: Long) : NavigationConfig {
            override fun equalsIgnoreScreenId(other: NavigationConfig) = other is InstanceList
        }

        @Serializable
        data class Search(val screenId: Long) : NavigationConfig {
            override fun equalsIgnoreScreenId(other: NavigationConfig) = other is Search
        }

        @Serializable
        data class Configuration(val screenId: Long) : NavigationConfig {
            override fun equalsIgnoreScreenId(other: NavigationConfig) = other is Configuration
        }

        @Serializable
        data class InstanceDetails(val screenId: Long, val instance: InstanceData) : NavigationConfig {
            override fun equalsIgnoreScreenId(other: NavigationConfig) =
                other is InstanceDetails && instance == other.instance
        }

        @Serializable
        data class ProjectDetails(val screenId: Long, val project: ProjectData) : NavigationConfig {
            override fun equalsIgnoreScreenId(other: NavigationConfig) =
                other is ProjectDetails && project == other.project
        }
    }

    @Serializable
    private data class NavigationState(
        val configurations: List<NavigationConfig>,
        val index: Int
    ) : NavState<NavigationConfig> {
        override val children: List<ChildNavState<NavigationConfig>> by lazy {
            configurations.mapIndexed { index, screenConfig ->
                SimpleChildNavState(
                    configuration = screenConfig,
                    status = if (index == this.index) ChildNavState.Status.RESUMED else ChildNavState.Status.CREATED
                )
            }
        }
    }
}
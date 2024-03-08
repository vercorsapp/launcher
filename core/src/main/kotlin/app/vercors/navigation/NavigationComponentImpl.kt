package app.vercors.navigation

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.Refreshable
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationComponent
import app.vercors.home.HomeComponent
import app.vercors.instance.InstanceListComponent
import app.vercors.instance.details.InstanceDetailsComponent
import app.vercors.project.ProjectDetailsComponent
import app.vercors.project.SearchComponent
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class NavigationComponentImpl(
    componentContext: AppComponentContext,
    private val navigationService: NavigationService = componentContext.inject()
) : AbstractAppComponent(componentContext), NavigationEventHandler by navigationService, NavigationComponent {
    private val navigation = StackNavigation<NavigationConfig>()
    private val _children: Value<ChildStack<NavigationConfig, NavigationChildComponent>> = childStack(
        source = navigation,
        serializer = NavigationConfig.serializer(),
        initialConfiguration = navigationService.navigationState.value.active,
        childFactory = { configuration, componentContext ->
            createChild(configuration, appChildContext(componentContext))
        }
    )
    override val children: Value<ChildStack<*, NavigationChildComponent>> = _children

    override fun onCreate() {
        super.onCreate()
        launch {
            navigationService.navigationState.filterNotNull().collect {
                navigation.bringToFront(it.active)
            }
        }
        launch {
            navigationService.refreshChannel.consumeEach {
                val active = children.active.instance
                if (active is Refreshable) active.refresh()
            }
        }
    }

    private fun createChild(config: NavigationConfig, componentContext: AppComponentContext): NavigationChildComponent =
        when (config) {
            NavigationConfig.Home -> inject<HomeComponent>(componentContext)
            NavigationConfig.InstanceList -> inject<InstanceListComponent>(componentContext)
            NavigationConfig.Search -> inject<SearchComponent>(componentContext)
            NavigationConfig.Configuration -> inject<ConfigurationComponent>(componentContext)
            is NavigationConfig.InstanceDetails -> inject<InstanceDetailsComponent>(componentContext)
            is NavigationConfig.ProjectDetails -> inject<ProjectDetailsComponent>(componentContext)
        }
}
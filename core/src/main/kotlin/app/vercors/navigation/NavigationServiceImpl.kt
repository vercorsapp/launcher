package app.vercors.navigation

import app.vercors.common.AppTab
import app.vercors.configuration.ConfigurationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationServiceImpl(
    coroutineScope: CoroutineScope,
    private val configurationService: ConfigurationService
) : NavigationService, CoroutineScope by coroutineScope {
    private val _navigationState: MutableStateFlow<NavigationState> by lazy {
        MutableStateFlow(
            NavigationState(
                items = listOf(getConfigForTab(configurationService.config.defaultTab)),
                index = 0
            )
        )
    }
    override val navigationState: StateFlow<NavigationState> by lazy { _navigationState }
    private val _refreshChannel: Channel<Unit> = Channel()
    override val refreshChannel: ReceiveChannel<Unit> = _refreshChannel

    override fun handle(event: NavigationEvent) {
        when (event) {
            NavigationEvent.Next -> _navigationState.update { it.copy(index = it.index + 1) }
            NavigationEvent.Previous -> _navigationState.update { it.copy(index = it.index - 1) }
            NavigationEvent.Refresh -> {
                launch { _refreshChannel.send(Unit) }
            }

            else -> navigateTo(getConfigForEvent(event))
        }
    }

    override fun navigateTo(config: NavigationConfig) {
        _navigationState.update {
            if (it.active == config) it
            else it.copy(items = it.items.take(it.index + 1) + config, index = it.index + 1)
        }
    }

    private fun getConfigForTab(tab: AppTab): NavigationConfig = when (tab) {
        AppTab.Home -> NavigationConfig.Home
        AppTab.Instances -> NavigationConfig.InstanceList
        AppTab.Search -> NavigationConfig.Search
        AppTab.Settings -> NavigationConfig.Configuration
    }

    private fun getConfigForEvent(event: NavigationEvent): NavigationConfig = when (event) {
        NavigationEvent.Home -> NavigationConfig.Home
        NavigationEvent.InstanceList -> NavigationConfig.InstanceList
        NavigationEvent.Search -> NavigationConfig.Search
        NavigationEvent.Configuration -> NavigationConfig.Configuration
        is NavigationEvent.InstanceDetails -> NavigationConfig.InstanceDetails(event.instance)
        is NavigationEvent.ProjectDetails -> NavigationConfig.ProjectDetails(event.project)
        else -> throw IllegalArgumentException("Can't create NavigationConfig for NavigationEvent $event")
    }
}
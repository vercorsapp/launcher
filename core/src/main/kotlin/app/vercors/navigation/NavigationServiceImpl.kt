package app.vercors.navigation

import app.vercors.common.AppTab
import app.vercors.configuration.ConfigurationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NavigationServiceImpl(configurationService: ConfigurationService) : NavigationService {
    private val _state = MutableStateFlow<NavigationChildren<*, NavigationChildComponent>?>(null)
    override val state: StateFlow<NavigationChildren<*, NavigationChildComponent>?> = _state
    private val _currentTab = MutableStateFlow(configurationService.configState.value?.defaultTab ?: AppTab.Home)
    override val currentTab: StateFlow<AppTab> = _currentTab

    override fun onNavigate(children: NavigationChildren<*, NavigationChildComponent>) {
        _state.update { children }
        _currentTab.update { children.active.instance.tab }
    }
}
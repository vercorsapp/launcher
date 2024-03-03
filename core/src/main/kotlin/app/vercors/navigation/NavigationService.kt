package app.vercors.navigation

import app.vercors.common.AppTab
import kotlinx.coroutines.flow.StateFlow

interface NavigationService {
    val currentTab: StateFlow<AppTab>
    val state: StateFlow<NavigationChildren<*, NavigationChildComponent>?>
    fun onNavigate(children: NavigationChildren<*, NavigationChildComponent>)
}
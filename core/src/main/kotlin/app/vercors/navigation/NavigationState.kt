package app.vercors.navigation

import app.vercors.common.AppTab

data class NavigationState(
    val items: List<NavigationConfig>,
    val index: Int
) {
    val active: NavigationConfig = items[index]
    val currentTab: AppTab = active.tab
    val hasPreviousScreen = index > 0
    val hasNextScreen = index < items.size - 1
}
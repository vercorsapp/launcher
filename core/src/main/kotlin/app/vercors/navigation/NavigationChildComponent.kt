package app.vercors.navigation

import app.vercors.common.AppTab
import app.vercors.toolbar.ToolbarTitle

interface NavigationChildComponent {
    val tab: AppTab
    val isDefault: Boolean
    val title: ToolbarTitle
}
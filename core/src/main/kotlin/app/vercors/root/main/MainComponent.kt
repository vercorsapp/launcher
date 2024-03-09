package app.vercors.root.main

import app.vercors.account.AccountListComponent
import app.vercors.dialog.DialogComponent
import app.vercors.menu.MenuComponent
import app.vercors.navigation.NavigationComponent
import app.vercors.notification.NotificationListComponent
import app.vercors.root.RootChildComponent
import app.vercors.toolbar.ToolbarComponent
import kotlinx.coroutines.flow.StateFlow

interface MainComponent : RootChildComponent {
    val uiState: StateFlow<MainUiState>
    val menuComponent: MenuComponent
    val toolbarComponent: ToolbarComponent
    val navigationComponent: NavigationComponent
    val accountListComponent: AccountListComponent
    val dialogComponent: DialogComponent
    val notificationListComponent: NotificationListComponent
    fun onMaximize()
    fun windowEventProcessed()
}
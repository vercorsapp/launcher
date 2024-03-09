package app.vercors.menu

import app.vercors.account.AccountService
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.DialogEvent
import app.vercors.dialog.DialogService
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MenuComponentImpl(
    componentContext: AppComponentContext,
    private val onAccountsMenuButtonClick: () -> Unit,
    private val navigationService: NavigationService = componentContext.inject(),
    private val accountService: AccountService = componentContext.inject(),
    private val dialogService: DialogService = componentContext.inject()
) : AbstractAppComponent(componentContext), MenuComponent {
    private val _uiState = MutableStateFlow(
        MenuUiState(
            topButtons = listOf(
                MenuButton.Home,
                MenuButton.Instances,
                MenuButton.Search,
                MenuButton.CreateInstance
            ),
            selectedTab = navigationService.navigationState.value.currentTab,
            selectedAccount = accountService.selectedAccountState.value,
            bottomButtons = listOf(MenuButton.Settings, MenuButton.Accounts)
        )
    )
    override val uiState: StateFlow<MenuUiState> = _uiState

    init {
        navigationService.navigationState.map { it.active.tab }.collectInLifecycle { tab ->
            _uiState.update { it.copy(selectedTab = tab) }
        }
        accountService.selectedAccountState.collectInLifecycle { account ->
            _uiState.update { it.copy(selectedAccount = account) }
        }
    }

    override fun onMenuButtonClick(button: MenuButton) {
        when (button) {
            MenuButton.Accounts -> onAccountsMenuButtonClick()
            MenuButton.CreateInstance -> dialogService.openDialog(DialogEvent.CreateInstance)
            MenuButton.Home -> navigationService.handle(NavigationEvent.Home)
            MenuButton.Instances -> navigationService.handle(NavigationEvent.InstanceList)
            MenuButton.Search -> navigationService.handle(NavigationEvent.Search)
            MenuButton.Settings -> navigationService.handle(NavigationEvent.Configuration)
        }
    }
}
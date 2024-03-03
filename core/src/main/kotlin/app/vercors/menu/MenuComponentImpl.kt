package app.vercors.menu

import app.vercors.account.AccountService
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.navigation.NavigationService
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuComponentImpl(
    componentContext: AppComponentContext,
    override val onMenuButtonClick: (MenuButton) -> Unit,
    private val navigationService: NavigationService = componentContext.inject(),
    private val accountService: AccountService = componentContext.inject()
) : AbstractAppComponent(componentContext), MenuComponent {
    private val _uiState = MutableStateFlow(
        MenuUiState(
            topButtons = persistentListOf(
                MenuButton.Home,
                MenuButton.Instances,
                MenuButton.Search,
                MenuButton.CreateInstance
            ),
            selectedTab = navigationService.currentTab.value,
            selectedAccount = accountService.selectedAccountState.value,
            bottomButtons = persistentListOf(MenuButton.Settings, MenuButton.Accounts)
        )
    )
    override val uiState: StateFlow<MenuUiState> = _uiState

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            navigationService.currentTab.collect { tab ->
                _uiState.update { it.copy(selectedTab = tab) }
            }
        }
        scope.launch {
            accountService.selectedAccountState.collect { account ->
                _uiState.update { it.copy(selectedAccount = account) }
            }
        }
    }
}
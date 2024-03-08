package app.vercors.account

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.DialogEvent
import app.vercors.dialog.DialogService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountsComponentImpl(
    componentContext: AppComponentContext,
    private val accountService: AccountService = componentContext.inject(),
    private val dialogService: DialogService = componentContext.inject()
) : AbstractAppComponent(componentContext), AccountsComponent {
    private val _uiState: MutableStateFlow<AccountsUiState> = MutableStateFlow(AccountsUiState())
    override val uiState: StateFlow<AccountsUiState> = _uiState

    override fun onCreate() {
        super.onCreate()
        launch {
            accountService.accountListState.filterNotNull().collect { accounts ->
                _uiState.update { it.copy(data = accounts) }
            }
        }
    }

    override fun onTogglePopup() {
        _uiState.update { it.copy(isPopupOpen = !it.isPopupOpen) }
    }

    override fun onSelectAccount(account: AccountData) {
        accountService.selectAccount(account)
    }

    override fun onRemoveAccount(account: AccountData) {
        accountService.removeAccount(account)
    }

    override fun onAddAccount() {
        onTogglePopup()
        dialogService.openDialog(DialogEvent.Login)
    }
}
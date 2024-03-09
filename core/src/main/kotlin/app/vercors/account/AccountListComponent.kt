package app.vercors.account

import kotlinx.coroutines.flow.StateFlow

interface AccountListComponent {
    val uiState: StateFlow<AccountsUiState>
    fun onTogglePopup()
    fun onSelectAccount(account: AccountData)
    fun onRemoveAccount(account: AccountData)
    fun onAddAccount()
}

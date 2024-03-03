package app.vercors.account

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface AccountService {
    val loadingState: StateFlow<AccountLoadingState>
    val accountListState: StateFlow<AccountListData?>
    val selectedAccountState: StateFlow<AccountData?>

    fun loadAccounts(): Job
    fun addAccount(account: AccountData)
    fun removeAccount(account: AccountData)
    fun selectAccount(account: AccountData)
}
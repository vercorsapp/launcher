package app.vercors.account

import app.vercors.onError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountServiceImpl(
    coroutineScope: CoroutineScope,
    private val accountRepository: AccountRepository
) : AccountService, CoroutineScope by coroutineScope {
    private val _loadingState: MutableStateFlow<AccountLoadingState> = MutableStateFlow(AccountLoadingState.NotLoaded)
    override val loadingState: StateFlow<AccountLoadingState> = _loadingState
    override val accountListState: StateFlow<AccountListData?> = loadingState
        .filterIsInstance<AccountLoadingState.Loaded>()
        .map { it.data }
        .stateIn(this, SharingStarted.Eagerly, null)
    override val selectedAccountState: StateFlow<AccountData?> = accountListState
        .filterNotNull()
        .map { state -> state.selected?.let { state.accounts.find { account -> account.uuid == it } } }
        .stateIn(this, SharingStarted.Eagerly, null)

    init {
        launch { accountListState.filterNotNull().drop(1).collect { save(it) } }
    }

    override fun loadAccounts(): Job = launch {
        val accounts = accountRepository.loadAccounts()
        _loadingState.update { AccountLoadingState.Loaded(accounts) }
    }.onError { error -> _loadingState.update { AccountLoadingState.Error(error) } }

    override fun addAccount(account: AccountData) {
        if (account.uuid in accountListState.value!!.accounts.map { it.uuid }) {
            updateAccounts {
                it.copy(
                    selected = account.uuid,
                    accounts = it.accounts.map { oldAccount -> if (oldAccount.uuid == account.uuid) account else oldAccount }
                )
            }
        } else {
            updateAccounts { it.copy(selected = account.uuid, accounts = it.accounts + account) }
        }
    }

    override fun removeAccount(account: AccountData) {
        updateAccounts { it.copy(accounts = it.accounts - account) }
    }

    override fun selectAccount(account: AccountData) {
        updateAccounts { it.copy(selected = account.uuid) }
    }

    private fun updateAccounts(updater: (AccountListData) -> AccountListData) {
        _loadingState.update {
            when (it) {
                is AccountLoadingState.Loaded -> AccountLoadingState.Loaded(validateAccountData(updater(it.data)))
                else -> it
            }
        }
    }

    private fun validateAccountData(data: AccountListData): AccountListData {
        return if (data.accounts.isEmpty() && data.selected != null)
            data.copy(selected = null)
        else if (data.accounts.isNotEmpty() && (data.selected == null || data.selected !in data.accounts.map { it.uuid }))
            data.copy(selected = data.accounts.first().uuid)
        else
            data
    }

    private fun save(accountState: AccountListData) {
        launch { accountRepository.saveAccounts(accountState) }
    }
}
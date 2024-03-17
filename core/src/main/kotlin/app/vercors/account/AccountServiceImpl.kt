/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
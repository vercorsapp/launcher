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

import app.vercors.common.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class AccountRepositoryImpl(
    private val externalScope: CoroutineScope,
    private val dataSource: AccountDataSource
) : AccountRepository {
    private val _state: MutableStateFlow<Resource<AccountList>> = createLoadableStateFlow()
    override val loadingState: StateFlow<Resource<AccountList>> = _state
    override val state: StateFlow<AccountList?> = loadingState.toResultStateFlow(externalScope)
    override val current: AccountList get() = state.value!!
    override val selectedState: StateFlow<Account?> = state
        .map { it?.findAccount(it.selected) }
        .stateIn(externalScope, SharingStarted.Eagerly, null)
    override val currentSelected: Account get() = selectedState.value!!

    override suspend fun loadAccounts() {
        externalScope.launch {
            try {
                val accounts = dataSource.loadAccounts()
                _state.emitLoaded(accounts)
            } catch (e: Exception) {
                _state.emitErrored(e)
            }
        }.join()
    }

    override suspend fun addAccount(account: Account) = updateAccounts {
        it.copy(
            accounts = it.accounts.filter { a -> a.uuid != account.uuid } + account,
            selected = account.uuid
        )
    }

    override suspend fun removeAccount(uuid: String) = updateAccounts {
        val newSelected =
            if (it.selected == uuid) it.accounts.map { a -> a.uuid }.firstOrNull()
            else it.selected
        it.copy(
            accounts = it.accounts.filter { a -> a.uuid != uuid },
            selected = newSelected
        )
    }

    override suspend fun selectAccount(uuid: String) = updateAccounts {
        it.findAccount(uuid)?.let { selectedAccount ->
            it.copy(selected = selectedAccount.uuid)
        } ?: it
    }

    override suspend fun updateAccounts(updater: (AccountList) -> AccountList) {
        externalScope.launch {
            val accounts = _state.updateLoadedAndGet(updater)
            if (accounts is Resource.Loaded) dataSource.saveAccounts(accounts.result)
        }.join()
    }
}
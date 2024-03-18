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

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.DialogConfig
import app.vercors.dialog.DialogService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update

class AccountListComponentImpl(
    componentContext: AppComponentContext,
    private val accountService: AccountService = componentContext.inject(),
    private val dialogService: DialogService = componentContext.inject()
) : AbstractAppComponent(componentContext), AccountListComponent {
    private val _state: MutableStateFlow<AccountListState> = MutableStateFlow(AccountListState())
    override val state: StateFlow<AccountListState> = _state

    init {
        accountService.accountListState.filterNotNull().collectInLifecycle { accounts ->
            _state.update { it.copy(data = accounts) }
        }
    }

    override fun onIntent(intent: AccountListIntent) = when (intent) {
        AccountListIntent.TogglePopup -> onTogglePopup()
        is AccountListIntent.SelectAccount -> onSelectAccount(intent.account)
        is AccountListIntent.RemoveAccount -> onRemoveAccount(intent.account)
        AccountListIntent.AddAccount -> onAddAccount()
    }

    private fun onTogglePopup() {
        _state.update { it.copy(isPopupOpen = !it.isPopupOpen) }
    }

    private fun onSelectAccount(account: AccountData) {
        accountService.selectAccount(account)
    }

    private fun onRemoveAccount(account: AccountData) {
        accountService.removeAccount(account)
    }

    private fun onAddAccount() {
        onTogglePopup()
        dialogService.onOpenDialog(DialogConfig.Login())
    }
}
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

package app.vercors.menu

import app.vercors.account.AccountService
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.DialogConfig
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
    accountService: AccountService = componentContext.inject(),
    private val dialogService: DialogService = componentContext.inject()
) : AbstractAppComponent(componentContext), MenuComponent {
    private val _state = MutableStateFlow(
        MenuState(
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
    override val state: StateFlow<MenuState> = _state

    init {
        navigationService.navigationState.map { it.active.tab }.collectInLifecycle { tab ->
            _state.update { it.copy(selectedTab = tab) }
        }
        accountService.selectedAccountState.collectInLifecycle { account ->
            _state.update { it.copy(selectedAccount = account) }
        }
    }

    override fun onIntent(intent: MenuIntent) = when (intent) {
        is MenuIntent.MenuButtonClick -> onMenuButtonClick(intent.button)
    }

    private fun onMenuButtonClick(button: MenuButton) {
        when (button) {
            MenuButton.Accounts -> onAccountsMenuButtonClick()
            MenuButton.CreateInstance -> dialogService.onOpenDialog(DialogConfig.CreateInstance)
            MenuButton.Home -> navigationService.handle(NavigationEvent.Home)
            MenuButton.Instances -> navigationService.handle(NavigationEvent.InstanceList)
            MenuButton.Search -> navigationService.handle(NavigationEvent.Search)
            MenuButton.Settings -> navigationService.handle(NavigationEvent.Configuration)
        }
    }
}
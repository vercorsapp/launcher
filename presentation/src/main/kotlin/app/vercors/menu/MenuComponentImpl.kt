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

import app.vercors.account.AccountRepository
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.DialogConfig
import app.vercors.dialog.DialogManager
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationManager
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class MenuComponentImpl(
    componentContext: AppComponentContext,
    private val onAccountsMenuButtonClick: () -> Unit,
    private val navigationManager: NavigationManager = componentContext.inject(),
    accountRepository: AccountRepository = componentContext.inject(),
    private val dialogManager: DialogManager = componentContext.inject()
) : AbstractAppComponent(componentContext, KotlinLogging.logger {}), MenuComponent {
    private val _state = MutableStateFlow(
        MenuState(
            topButtons = listOf(
                MenuButton.Home,
                MenuButton.Instances,
                MenuButton.Search,
                MenuButton.CreateInstance
            ),
            selectedTab = navigationManager.state.value.currentTab,
            selectedAccount = accountRepository.selectedState.value,
            bottomButtons = listOf(MenuButton.Settings, MenuButton.Accounts)
        )
    )
    override val state: StateFlow<MenuState> = _state

    init {
        navigationManager.state.map { it.active.tab }.collectInLifecycle { tab ->
            _state.update { it.copy(selectedTab = tab) }
        }
        accountRepository.selectedState.collectInLifecycle { account ->
            _state.update { it.copy(selectedAccount = account) }
        }
    }

    override fun onIntent(intent: MenuIntent) = when (intent) {
        is MenuIntent.MenuButtonClick -> onMenuButtonClick(intent.button)
    }

    private fun onMenuButtonClick(button: MenuButton) {
        when (button) {
            MenuButton.Accounts -> onAccountsMenuButtonClick()
            MenuButton.CreateInstance -> dialogManager.openDialog(DialogConfig.CreateInstance)
            MenuButton.Home -> navigationManager.handle(NavigationEvent.Home)
            MenuButton.Instances -> navigationManager.handle(NavigationEvent.InstanceList)
            MenuButton.Search -> navigationManager.handle(NavigationEvent.Search)
            MenuButton.Settings -> navigationManager.handle(NavigationEvent.Configuration)
        }
    }
}
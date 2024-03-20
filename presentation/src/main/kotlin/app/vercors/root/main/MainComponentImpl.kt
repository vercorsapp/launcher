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

package app.vercors.root.main

import app.vercors.account.AccountListComponent
import app.vercors.account.AccountListIntent
import app.vercors.account.AccountRepository
import app.vercors.common.*
import app.vercors.configuration.ConfigurationRepository
import app.vercors.dialog.DialogComponent
import app.vercors.dialog.DialogConfig
import app.vercors.dialog.DialogManager
import app.vercors.instance.LoadInstancesUseCase
import app.vercors.menu.MenuComponent
import app.vercors.navigation.NavigationComponent
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationManager
import app.vercors.notification.NotificationListComponent
import app.vercors.notification.NotificationListIntent
import app.vercors.system.StorageManager
import app.vercors.system.SystemThemeManager
import app.vercors.toolbar.ToolbarButton
import app.vercors.toolbar.ToolbarComponent
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

internal class MainComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val configurationRepository: ConfigurationRepository = componentContext.inject(),
    private val loadInstancesUseCase: LoadInstancesUseCase = componentContext.inject(),
    private val accountRepository: AccountRepository = componentContext.inject(),
    private val systemThemeManager: SystemThemeManager = componentContext.inject(),
    private val navigationManager: NavigationManager = componentContext.inject(),
    private val dialogManager: DialogManager = componentContext.inject(),
    private val storageManager: StorageManager = componentContext.inject()
) : AbstractAppComponent(componentContext), MainComponent {
    private val dialogComponent = inject<DialogComponent>(childContext("dialog"))
    private val accountListComponent = inject<AccountListComponent>(childContext("account"))
    private val menuComponent = inject<MenuComponent>(childContext("menu"), ::onAccountsMenuButtonClick)
    private val toolbarComponent =
        inject<ToolbarComponent>(childContext("toolbar"), ::onToolbarClick, ::onNotificationButtonClick)
    private val navigationComponent = inject<NavigationComponent>(childContext("navigation"))
    private val notificationListComponent = inject<NotificationListComponent>(childContext("notification"))
    override val children = MainChildComponents(
        dialogComponent = dialogComponent,
        accountListComponent = accountListComponent,
        menuComponent = menuComponent,
        toolbarComponent = toolbarComponent,
        navigationComponent = navigationComponent,
        notificationListComponent = notificationListComponent
    )
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.NotLoaded)
    override val state: StateFlow<MainState> = _state
    private val _event = Channel<MainEvent>()
    override val events: Flow<MainEvent> = _event.receiveAsFlow()

    init {
        // Load instances and accounts after configuration is loaded
        localScope.launch {
            configurationRepository.state.filterNotNull().first().let {
                listOf(
                    launch { loadInstancesUseCase() },
                    launch { accountRepository.loadAccounts() }
                ).joinAll()
            }
        }
        // Update UI state on configuration / system theme change
        localScope.launch {
            configurationRepository.state.filterNotNull().combine(systemThemeManager.darkState) { config, isDark ->
                val theme =
                    if (config.theme === AppTheme.System) (if (isDark) AppTheme.Dark else AppTheme.Light) else config.theme
                config to if (theme === AppTheme.Light) AppPalette.Latte else when (config.darkTheme) {
                    AppDarkTheme.Lighter -> AppPalette.Frappe
                    AppDarkTheme.Normal -> AppPalette.Macchiato
                    AppDarkTheme.Darker -> AppPalette.Mocha
                }
            }.collect { (config, palette) ->
                _state.update {
                    MainState.Loaded(
                        config = config,
                        palette = palette,
                        cachePath = storageManager.coilCachePath,
                    )
                }
            }
        }
    }

    override fun onIntent(intent: MainIntent) = when (intent) {
        MainIntent.Maximize -> onMaximize()
        MainIntent.Close -> onClose()
    }

    private fun onAccountsMenuButtonClick() {
        if (accountRepository.current.selected == null) {
            dialogManager.openDialog(DialogConfig.Login())
        } else {
            accountListComponent.onIntent(AccountListIntent.TogglePopup)
        }
    }

    private fun onToolbarClick(toolbarButton: ToolbarButton) {
        when (toolbarButton) {
            ToolbarButton.Previous -> navigationManager.handle(NavigationEvent.Previous)
            ToolbarButton.Next -> navigationManager.handle(NavigationEvent.Next)
            ToolbarButton.Refresh -> navigationManager.handle(NavigationEvent.Refresh)
            ToolbarButton.Minimize -> onSendEvent(MainEvent.Minimize)
            ToolbarButton.Maximize -> onMaximize()
            ToolbarButton.Close -> onSendEvent(MainEvent.Close)
        }
    }

    private fun onNotificationButtonClick() {
        notificationListComponent.onIntent(NotificationListIntent.TogglePopup)
    }

    private fun onMaximize() {
        onSendEvent(MainEvent.Maximize)
    }

    private fun onSendEvent(event: MainEvent) {
        localScope.launch { _event.send(event) }
    }
}
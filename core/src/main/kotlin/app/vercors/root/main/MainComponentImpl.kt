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
import app.vercors.account.AccountService
import app.vercors.common.*
import app.vercors.configuration.ConfigurationService
import app.vercors.dialog.DialogComponent
import app.vercors.dialog.DialogConfig
import app.vercors.dialog.DialogIntent
import app.vercors.instance.InstanceLoadingState
import app.vercors.instance.InstanceService
import app.vercors.menu.MenuComponent
import app.vercors.navigation.NavigationComponent
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationService
import app.vercors.notification.*
import app.vercors.system.storage.StorageService
import app.vercors.system.theme.SystemThemeService
import app.vercors.toolbar.ToolbarButton
import app.vercors.toolbar.ToolbarComponent
import com.arkivanov.decompose.value.ObserveLifecycleMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val configurationService: ConfigurationService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject(),
    private val accountService: AccountService = componentContext.inject(),
    private val systemThemeService: SystemThemeService = componentContext.inject(),
    private val storageService: StorageService = componentContext.inject(),
    private val notificationService: NotificationService = componentContext.inject(),
    private val navigationService: NavigationService = componentContext.inject()
) : AbstractAppComponent(componentContext), MainComponent {
    private val dialogComponent = inject<DialogComponent>(appChildContext("dialog"))
    private val accountListComponent = inject<AccountListComponent>(appChildContext("account"))
    private val menuComponent = inject<MenuComponent>(appChildContext("menu"), ::onAccountsMenuButtonClick)
    private val toolbarComponent =
        inject<ToolbarComponent>(appChildContext("toolbar"), ::onToolbarClick, ::onNotificationButtonClick)
    private val navigationComponent = inject<NavigationComponent>(appChildContext("navigation"))
    private val notificationListComponent = inject<NotificationListComponent>(appChildContext("notification"))
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
        launch {
            configurationService.configState.filterNotNull().first().let {
                instanceService.loadInstances()
                accountService.loadAccounts()
            }
        }
        // Update UI state on configuration / system theme change
        launch {
            configurationService.configState.filterNotNull().combine(systemThemeService.isDark) { config, isDark ->
                val theme =
                    if (config.theme === app.vercors.common.AppTheme.System) (if (isDark) app.vercors.common.AppTheme.Dark else app.vercors.common.AppTheme.Light) else config.theme
                config to if (theme === app.vercors.common.AppTheme.Light) AppPalette.Latte else when (config.darkTheme) {
                    AppDarkTheme.Lighter -> AppPalette.Frappe
                    AppDarkTheme.Normal -> AppPalette.Macchiato
                    AppDarkTheme.Darker -> AppPalette.Mocha
                }
            }.collect { (config, palette) ->
                _state.update {
                    MainState.Loaded(
                        config = config,
                        palette = palette,
                        cachePath = storageService.coilCachePath,
                    )
                }
            }
        }
        instanceService.loadingState.collectInLifecycle(ObserveLifecycleMode.CREATE_DESTROY) { state ->
            if (state is InstanceLoadingState.Loaded) {
                state.warns.forEach {
                    notificationService.sendNotification(
                        NotificationData(
                            NotificationLevel.WARN,
                            NotificationText.Template.InstanceNotFound,
                            arrayOf(it)
                        )
                    )
                }
                state.errors.forEach {
                    notificationService.sendNotification(
                        NotificationData(
                            NotificationLevel.ERROR,
                            NotificationText.Template.Error,
                            arrayOf(it.localizedMessage)
                        )
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
        if (accountService.selectedAccountState.value == null) {
            dialogComponent.onIntent(DialogIntent.OpenDialog(DialogConfig.Login()))
        } else {
            accountListComponent.onIntent(AccountListIntent.TogglePopup)
        }
    }

    private fun onToolbarClick(toolbarButton: ToolbarButton) {
        when (toolbarButton) {
            ToolbarButton.Previous -> navigationService.handle(NavigationEvent.Previous)
            ToolbarButton.Next -> navigationService.handle(NavigationEvent.Next)
            ToolbarButton.Refresh -> navigationService.handle(NavigationEvent.Refresh)
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
        launch { _event.send(event) }
    }
}
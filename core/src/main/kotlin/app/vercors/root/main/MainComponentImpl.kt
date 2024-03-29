package app.vercors.root.main

import app.vercors.account.AccountListComponent
import app.vercors.account.AccountService
import app.vercors.common.*
import app.vercors.configuration.ConfigurationService
import app.vercors.dialog.DialogComponent
import app.vercors.dialog.DialogEvent
import app.vercors.instance.InstanceLoadingState
import app.vercors.instance.InstanceService
import app.vercors.menu.MenuComponent
import app.vercors.navigation.NavigationComponent
import app.vercors.navigation.NavigationEvent
import app.vercors.notification.NotificationData
import app.vercors.notification.NotificationLevel
import app.vercors.notification.NotificationListComponent
import app.vercors.notification.NotificationText
import app.vercors.system.storage.StorageService
import app.vercors.system.theme.SystemThemeService
import app.vercors.toolbar.ToolbarButton
import app.vercors.toolbar.ToolbarComponent
import com.arkivanov.decompose.value.ObserveLifecycleMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val configurationService: ConfigurationService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject(),
    private val accountService: AccountService = componentContext.inject(),
    private val systemThemeService: SystemThemeService = componentContext.inject(),
    private val storageService: StorageService = componentContext.inject()
) : AbstractAppComponent(componentContext), MainComponent {
    override val dialogComponent = inject<DialogComponent>(appChildContext("dialog"))
    override val accountListComponent = inject<AccountListComponent>(appChildContext("account"))
    override val menuComponent = inject<MenuComponent>(appChildContext("menu"), ::onAccountsMenuButtonClick)
    override val toolbarComponent =
        inject<ToolbarComponent>(appChildContext("toolbar"), ::onToolbarClick, ::onNotificationButtonClick)
    override val navigationComponent = inject<NavigationComponent>(appChildContext("navigation"))
    override val notificationListComponent = inject<NotificationListComponent>(appChildContext("notification"))
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.NotLoaded)
    override val uiState: StateFlow<MainUiState> = _uiState

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
                _uiState.update {
                    MainUiState.Loaded(config, palette, storageService.coilCachePath, null)
                }
            }
        }
        instanceService.loadingState.collectInLifecycle(ObserveLifecycleMode.CREATE_DESTROY) { state ->
            if (state is InstanceLoadingState.Loaded) {
                state.warns.forEach {
                    notificationListComponent.sendNotification(
                        NotificationData(
                            NotificationLevel.WARN,
                            NotificationText.Template.InstanceNotFound,
                            arrayOf(it)
                        )
                    )
                }
                state.errors.forEach {
                    notificationListComponent.sendNotification(
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

    private fun onAccountsMenuButtonClick() {
        if (accountService.selectedAccountState.value == null) {
            dialogComponent.openDialog(DialogEvent.Login)
        } else {
            accountListComponent.onTogglePopup()
        }
    }

    private fun onToolbarClick(toolbarButton: ToolbarButton) {
        when (toolbarButton) {
            ToolbarButton.Previous -> navigationComponent.handle(NavigationEvent.Previous)
            ToolbarButton.Next -> navigationComponent.handle(NavigationEvent.Next)
            ToolbarButton.Refresh -> navigationComponent.handle(NavigationEvent.Refresh)
            ToolbarButton.Minimize -> updateWindowEvent(MainWindowEvent.Minimize)
            ToolbarButton.Maximize -> onMaximize()
            ToolbarButton.Close -> updateWindowEvent(MainWindowEvent.Close)
        }
    }

    private fun onNotificationButtonClick() {
        notificationListComponent.onTogglePopup()
    }

    override fun onMaximize() {
        updateWindowEvent(MainWindowEvent.Maximize)
    }

    override fun windowEventProcessed() {
        updateWindowEvent(null)
    }

    private fun updateWindowEvent(windowEvent: MainWindowEvent?) {
        _uiState.update { (it as MainUiState.Loaded).copy(windowEvent = windowEvent) }
    }
}
package app.vercors.root.main

import app.vercors.account.AccountService
import app.vercors.account.AccountsComponent
import app.vercors.common.*
import app.vercors.configuration.ConfigurationService
import app.vercors.dialog.DialogComponent
import app.vercors.dialog.DialogEvent
import app.vercors.instance.InstanceData
import app.vercors.instance.InstanceService
import app.vercors.instance.launch.LauncherService
import app.vercors.menu.MenuButton
import app.vercors.menu.MenuComponent
import app.vercors.navigation.NavigationChildComponent
import app.vercors.navigation.NavigationChildren
import app.vercors.navigation.NavigationComponent
import app.vercors.navigation.NavigationEvent
import app.vercors.project.ProjectData
import app.vercors.system.theme.SystemThemeService
import app.vercors.toolbar.ToolbarButton
import app.vercors.toolbar.ToolbarComponent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.nio.file.Path

class MainComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val configurationService: ConfigurationService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject(),
    private val accountService: AccountService = componentContext.inject(),
    private val systemThemeService: SystemThemeService = componentContext.inject(),
    private val launcherService: LauncherService = componentContext.inject()
) : AbstractAppComponent(componentContext), MainComponent {
    override val dialogComponent = inject<DialogComponent>(appChildContext("dialog"))
    override val accountsComponent = inject<AccountsComponent>(appChildContext("accounts"), ::onAddAccount)
    override val menuComponent = inject<MenuComponent>(appChildContext("menu"), ::onMenuButtonClick)
    override val toolbarComponent = inject<ToolbarComponent>(appChildContext("toolbar"), ::onToolbarClick)
    override val navigationComponent = inject<NavigationComponent>(
        appChildContext("navigation"),
        ::onNavigationChange,
        ::onLaunchInstance,
        ::onInstallProject,
        dialogComponent::openDialog
    )
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.NotLoaded)
    override val uiState: StateFlow<MainUiState> = _uiState

    init {
        // Load instances and accounts after configuration is loaded
        scope.launch {
            configurationService.configState.filterNotNull().first().let {
                instanceService.loadInstances()
                accountService.loadAccounts()
            }
        }
        // Update UI state on configuration / system theme change
        scope.launch {
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
                    MainUiState.Loaded(
                        config,
                        palette,
                        Path.of(config.path!!, "cache"),
                        null
                    )
                }
            }
        }
    }

    private fun onMenuButtonClick(menuButton: MenuButton) {
        when (menuButton) {
            MenuButton.Accounts -> {
                if (accountService.selectedAccountState.value == null) {
                    dialogComponent.openDialog(DialogEvent.Login)
                } else {
                    accountsComponent.onTogglePopup()
                }
            }

            MenuButton.CreateInstance -> dialogComponent.openDialog(DialogEvent.CreateInstance)
            MenuButton.Home -> navigationComponent.navigate(NavigationEvent.Home)
            MenuButton.Instances -> navigationComponent.navigate(NavigationEvent.InstanceList)
            MenuButton.Search -> navigationComponent.navigate(NavigationEvent.Search)
            MenuButton.Settings -> navigationComponent.navigate(NavigationEvent.Configuration)
        }
    }

    private fun onToolbarClick(toolbarButton: ToolbarButton) {
        when (toolbarButton) {
            ToolbarButton.Previous -> navigationComponent.navigate(NavigationEvent.Previous)
            ToolbarButton.Next -> navigationComponent.navigate(NavigationEvent.Next)
            ToolbarButton.Refresh -> navigationComponent.navigate(NavigationEvent.Refresh)
            ToolbarButton.Minimize -> updateWindowEvent(MainWindowEvent.Minimize)
            ToolbarButton.Maximize -> onMaximize()
            ToolbarButton.Close -> updateWindowEvent(MainWindowEvent.Close)
        }
    }

    private fun onNavigationChange(children: NavigationChildren<*, NavigationChildComponent>) {
        toolbarComponent.updateState(
            title = children.active.instance.title,
            hasPreviousScreen = children.hasPreviousScreen,
            hasNextScreen = children.hasNextScreen,
            canRefreshScreen = children.canRefreshScreen
        )
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

    private fun onAddAccount() {
        dialogComponent.openDialog(DialogEvent.Login)
    }

    private fun onLaunchInstance(instance: InstanceData) {
        scope.launch { launcherService.launch(instance).collect() }
    }

    private fun onInstallProject(project: ProjectData) {
        // TODO
    }
}
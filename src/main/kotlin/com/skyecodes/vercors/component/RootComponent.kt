package com.skyecodes.vercors.component

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.children.*
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.jthemedetecor.OsThemeDetector
import com.skyecodes.vercors.component.dialog.*
import com.skyecodes.vercors.component.screen.*
import com.skyecodes.vercors.data.model.app.*
import com.skyecodes.vercors.data.service.*
import com.skyecodes.vercors.ui.Palette
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.awt.Dimension
import java.awt.Toolkit
import java.util.concurrent.atomic.AtomicLong

private val logger = KotlinLogging.logger { }

interface RootComponent {
    val children: Value<Children<*, ScreenChild>>
    val dialog: Value<ChildSlot<*, DialogChild>>
    val uiState: StateFlow<UiState>
    val configurationState: StateFlow<ConfigurationState>
    val configuration: StateFlow<Configuration>
    val instancesState: StateFlow<InstancesState>
    val instances: StateFlow<List<Instance>>
    val activeTab: StateFlow<AppTab?>
    val accountData: StateFlow<AccountData>
    val currentAccount: Flow<Account?>

    fun initializeWindowState(windowState: WindowState)
    fun initializeWindow(window: ComposeWindow)
    fun onMinimize()
    fun onMaximize()
    fun navigate(tab: AppTab)
    fun updateConfiguration(config: Configuration)
    fun onNextScreen()
    fun onPreviousScreen()
    fun onRefreshScreen()
    fun openNewInstanceDialog()
    fun toggleAccountsPopupOrOpenDialog()
    fun toggleAccountsPopup()
    fun openAddAccountDialog()
    fun selectAccount(account: Account)
    fun removeAccount(account: Account)
    fun openErrorDialog(title: String, vararg message: String)
    fun closeDialog()

    sealed class ScreenChild(val tab: AppTab? = null, val isDefault: Boolean = false) {
        data object None : ScreenChild()
        class Home(val component: HomeComponent) : ScreenChild(AppTab.Home, true), Refreshable by component
        class Instances(val component: InstancesComponent) : ScreenChild(AppTab.Instances, true),
            Refreshable by component
        class Search(val component: SearchComponent) : ScreenChild(AppTab.Search, true), Refreshable by component
        class Settings(val component: SettingsComponent) : ScreenChild(AppTab.Settings, true), Refreshable by component
        class InstanceDetails(val component: InstanceDetailsComponent) : ScreenChild(AppTab.Instances),
            Refreshable by component
    }

    sealed interface DialogChild {
        class CreateNewInstance(val component: CreateNewInstanceDialogComponent) : DialogChild
        class AddAccount(val component: AddAccountDialogComponent) : DialogChild
        class Error(val component: ErrorDialogComponent) : DialogChild
    }

    class Children<out C : Any, out T : Any>(
        val items: List<Child.Created<C, T>>,
        val index: Int
    ) {
        val active: Child.Created<C, T> = items[index]
        val hasPreviousScreen = index > 0
        val hasNextScreen = index < items.size - 1
        val canRefreshScreen = active.instance is Refreshable
    }

    data class UiState(
        val palette: Palette,
        val isFirstNavigation: Boolean = true,
        val fatalError: Throwable? = null,
        val error: Throwable? = null,
        val accountsPopupOpen: Boolean = false
    )
}

interface Refreshable {
    fun refresh()
}

class DefaultRootComponent(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.get(),
    private val instanceService: InstanceService = componentContext.get(),
    private val accountService: AccountService = componentContext.get()
) : AbstractComponent(componentContext), RootComponent {
    private lateinit var window: ComposeWindow
    private lateinit var windowState: WindowState
    private lateinit var savedPos: WindowPosition
    private lateinit var savedWindowMode: WindowPlacement
    private lateinit var savedSize: Dimension
    private lateinit var cancellation: Cancellation
    private val detector = OsThemeDetector.getDetector()
    private val detectorListener: (Boolean) -> Unit = {
        val state = configurationState.value
        if (state is ConfigurationState.Loaded && state.config.theme === AppTheme.System) updatePalette(state.config)
    }
    private var isDetectorRegistered = false
    private val screenId = AtomicLong(0)
    private val nextScreenId get() = screenId.getAndIncrement()
    private val screenNavigation = SimpleNavigation<(AppNavigationState) -> AppNavigationState>()

    override val uiState: MutableStateFlow<RootComponent.UiState> =
        MutableStateFlow(RootComponent.UiState(Palette.Mocha))
    override val configurationState: StateFlow<ConfigurationState> = configurationService.state
    override lateinit var configuration: StateFlow<Configuration>
    override val instancesState: StateFlow<InstancesState> = instanceService.state
    override lateinit var instances: StateFlow<List<Instance>>
    override val activeTab: MutableStateFlow<AppTab?> = MutableStateFlow(null)
    override val children: Value<RootComponent.Children<ScreenConfig, RootComponent.ScreenChild>> = children(
        source = screenNavigation,
        stateSerializer = AppNavigationState.serializer(),
        key = "screenNavigation",
        initialState = {
            AppNavigationState(
                configurations = listOf(ScreenConfig.None),
                index = 0
            )
        },
        navTransformer = { state, transformer -> transformer(state) },
        stateMapper = { state, children ->
            RootComponent.Children(
                items = children.map { it as Child.Created },
                index = state.index
            )
        },
        backTransformer = {
            it.takeIf { it.index > 0 }?.let { state ->
                { state.copy(index = state.index - 1) }
            }
        },
        childFactory = { configuration, componentContext ->
            createChild(
                configuration,
                DefaultAppComponentContext(
                    componentContext = componentContext,
                    koin = koin
                )
            )
        }
    )
    private val activeChild get() = children.value.active.instance
    private val dialogNavigation = SlotNavigation<DialogConfig>()
    override val dialog: Value<ChildSlot<DialogConfig, RootComponent.DialogChild>> = childSlot(
        source = dialogNavigation,
        serializer = DialogConfig.serializer(),
        handleBackButton = true
    ) { configuration, componentContext ->
        createDialog(
            configuration,
            DefaultAppComponentContext(
                componentContext = componentContext,
                koin = koin
            )
        )
    }
    override val accountData: StateFlow<AccountData> = accountService.accountData
    override val currentAccount: Flow<Account?> =
        accountData.filterIsInstance<AccountData.Loaded>().map { it.selectedAccount }

    private val handler = CoroutineExceptionHandler { _, throwable ->
        logger.error(throwable) { "An error occured while loading the application." }
        uiState.update { it.copy(fatalError = throwable) }
    }

    init {
        lifecycle.doOnCreate(::onCreate)
        lifecycle.doOnDestroy(::onDestroy)
    }

    private fun onCreate() {
        logger.info { "Creating RootComponent" }
        scope.launch(handler) {
            configurationService.loadConfiguration()
            configuration = configurationService.config.stateIn(this)
            initNavigation(configuration.value.defaultTab)
            detector.registerListener(detectorListener)
            isDetectorRegistered = true
        }
        scope.launch(handler) {
            configurationState.collect { state ->
                when (state) {
                    is ConfigurationState.Errored -> uiState.update { it.copy(fatalError = state.error) }
                    is ConfigurationState.Loaded -> updatePalette(state.config)
                    ConfigurationState.NotLoaded -> {}
                }
            }
        }
        scope.launch(handler) {
            instanceService.loadInstances()
            instances = instanceService.instances.stateIn(this)
        }
        scope.launch(handler) {
            instancesState.collect { state ->
                when (state) {
                    is InstancesState.Loaded -> {
                        if (state.errors.isNotEmpty()) {
                            // TODO handle errors
                        } else if (state.warns.isNotEmpty()) {
                            // TODO handle warns
                        }
                    }

                    InstancesState.NotLoaded -> {}
                }
            }
        }
        scope.launch(handler) {
            accountService.loadAccounts()
        }
        cancellation = children.subscribe {
            it.active.instance.tab?.let { tab ->
                logger.info { "Navigated to ${tab.name} tab" }
                activeTab.value = tab
            }
        }
    }

    private fun onDestroy() {
        if (isDetectorRegistered) {
            detector.removeListener(detectorListener)
            isDetectorRegistered = false
        }
        cancellation.cancel()
    }

    private fun updatePalette(config: Configuration) {
        val palette = when (config.theme) {
            AppTheme.Dark -> Palette.Mocha
            AppTheme.Light -> Palette.Latte
            else -> if (detector.isDark) Palette.Mocha else Palette.Latte
        }
        uiState.update { uiState -> uiState.copy(palette = palette) }
    }

    override fun initializeWindowState(windowState: WindowState) {
        logger.info { "Initializing WindowState" }
        this.windowState = windowState
    }

    override fun initializeWindow(window: ComposeWindow) {
        logger.info { "Initializing window" }
        this.window = window
        window.minimumSize = Dimension(1280, 720)
        savedWindowMode = window.placement
        savedSize = window.size
        savedPos = windowState.position
    }

    override fun onMinimize() {
        windowState.isMinimized = true
    }

    override fun onMaximize() {
        savedWindowMode = if (savedWindowMode == WindowPlacement.Floating) {
            savedSize = window.size
            savedPos = windowState.position
            val insets = Toolkit.getDefaultToolkit().getScreenInsets(window.graphicsConfiguration)
            val screenBounds = window.graphicsConfiguration.bounds
            window.setSize(
                screenBounds.width - (insets.left + insets.right),
                screenBounds.height - (insets.top + insets.bottom)
            )
            window.setLocation(screenBounds.x + insets.left, screenBounds.y + insets.top)
            WindowPlacement.Maximized
        } else {
            window.size = savedSize
            windowState.position = savedPos
            WindowPlacement.Floating
        }
    }

    private fun initNavigation(tab: AppTab) {
        screenNavigation.navigate {
            it.copy(
                configurations = listOf(createDefaultConfigForTab(tab)),
                index = 0
            )
        }
        uiState.update { it.copy(isFirstNavigation = false) }
    }

    private fun createDefaultConfigForTab(tab: AppTab) = when (tab) {
        AppTab.Home -> ScreenConfig.Home(nextScreenId)
        AppTab.Instances -> ScreenConfig.Instances(nextScreenId)
        AppTab.Search -> ScreenConfig.Search(nextScreenId)
        AppTab.Settings -> ScreenConfig.Settings(nextScreenId)
    }

    override fun navigate(tab: AppTab) {
        if (activeTab.value === tab && activeChild.isDefault) return
        screenNavigation.navigate {
            it.copy(
                configurations = it.configurations.filterIndexed { i, _ -> i <= it.index } + createDefaultConfigForTab(
                    tab
                ),
                index = it.index + 1
            )
        }
    }

    override fun onNextScreen() {
        if (children.value.index < children.value.items.size - 1) {
            logger.info { "Navigating to next screen..." }
            screenNavigation.navigate { it.copy(index = it.index + 1) }
        }
    }

    override fun onPreviousScreen() {
        if (children.value.index > 0) {
            logger.info { "Navigating to previous screen..." }
            screenNavigation.navigate { it.copy(index = it.index - 1) }
        }
    }

    override fun onRefreshScreen() {
        logger.info { "Refreshing screen" }
        activeChild.let {
            if (it is Refreshable) it.refresh()
        }
    }

    override fun openNewInstanceDialog() {
        dialogNavigation.activate(DialogConfig.CreateNewInstance)
    }

    override fun toggleAccountsPopupOrOpenDialog() {
        val data = accountData.value
        if (data is AccountData.Loaded) {
            if (data.accounts.isEmpty()) {
                openAddAccountDialog()
            } else {
                toggleAccountsPopup()
            }
        }
    }

    override fun toggleAccountsPopup() {
        uiState.update { it.copy(accountsPopupOpen = !it.accountsPopupOpen) }
    }

    override fun openAddAccountDialog() {
        dialogNavigation.activate(DialogConfig.AddAccount)
    }

    override fun openErrorDialog(title: String, vararg message: String) {
        dialogNavigation.activate(DialogConfig.Error(title, message.toList()))
    }

    override fun closeDialog() {
        dialogNavigation.dismiss()
    }

    override fun updateConfiguration(config: Configuration) {
        configurationService.updateConfiguration(config)
    }

    override fun selectAccount(account: Account) {
        accountService.selectAccount(account)
    }

    override fun removeAccount(account: Account) {
        accountService.removeAccount(account) // TODO add confirmation dialog
    }

    private fun createChild(config: ScreenConfig, componentContext: AppComponentContext): RootComponent.ScreenChild =
        when (config) {
            is ScreenConfig.None -> RootComponent.ScreenChild.None
            is ScreenConfig.Home -> RootComponent.ScreenChild.Home(homeComponent(componentContext))
            is ScreenConfig.Instances -> RootComponent.ScreenChild.Instances(instancesComponent(componentContext))
            is ScreenConfig.Search -> RootComponent.ScreenChild.Search(searchComponent(componentContext))
            is ScreenConfig.Settings -> RootComponent.ScreenChild.Settings(settingsComponent(componentContext))
            is ScreenConfig.InstanceDetails -> RootComponent.ScreenChild.InstanceDetails(
                instanceDetailsComponent(
                    componentContext,
                    config.instance
                )
            )
        }

    private fun homeComponent(componentContext: AppComponentContext): HomeComponent = DefaultHomeComponent(
        componentContext = componentContext,
        showInstanceDetails = ::showInstanceDetails,
        launchInstance = ::launchInstance,
        showProjectDetails = ::showProjectDetails,
        installProject = ::installProject
    )

    private fun instancesComponent(componentContext: AppComponentContext): InstancesComponent =
        DefaultInstancesComponent(
            componentContext = componentContext,
            openNewInstanceDialog = ::openNewInstanceDialog,
            showInstanceDetails = ::showInstanceDetails,
            launchInstance = ::launchInstance,
            configuration = configuration,
            instances = instances
        )

    private fun searchComponent(componentContext: AppComponentContext): SearchComponent = DefaultSearchComponent(
        componentContext = componentContext
    )

    private fun settingsComponent(componentContext: AppComponentContext): SettingsComponent = DefaultSettingsComponent(
        componentContext = componentContext
    )

    private fun instanceDetailsComponent(
        componentContext: AppComponentContext,
        instance: Instance
    ): InstanceDetailsComponent = DefaultInstanceDetailsComponent(
        componentContext = componentContext,
        instance = instance
    )

    private fun createDialog(config: DialogConfig, componentContext: AppComponentContext): RootComponent.DialogChild =
        when (config) {
            is DialogConfig.CreateNewInstance -> RootComponent.DialogChild.CreateNewInstance(
                createNewInstanceComponent(componentContext)
            )

            is DialogConfig.AddAccount -> RootComponent.DialogChild.AddAccount(addAccountComponent(componentContext))
            is DialogConfig.Error -> RootComponent.DialogChild.Error(createErrorComponent(componentContext, config))
        }

    private fun createNewInstanceComponent(componentContext: AppComponentContext): CreateNewInstanceDialogComponent =
        DefaultCreateNewInstanceDialogComponent(
            componentContext = componentContext,
            onCreateInstance = ::onCreateInstance,
            onClose = ::closeDialog
        )

    private fun addAccountComponent(componentContext: AppComponentContext): AddAccountDialogComponent =
        DefaultAddAccountDialogComponent(
            componentContext = componentContext,
            onClose = ::closeDialog
        )

    private fun createErrorComponent(
        componentContext: AppComponentContext,
        config: DialogConfig.Error
    ): ErrorDialogComponent =
        DefaultErrorDialogComponent(
            componentContext = componentContext,
            title = config.title,
            message = config.message,
            onClose = ::closeDialog
        )

    private fun onCreateInstance(instance: Instance) {
        scope.launch {
            try {
                instanceService.createInstance(instance).await()
            } catch (e: Exception) {
                val title = "An error occured while creating instance"
                logger.error(e) { title }
                openErrorDialog(title, "Please check the logs for more details.", e.localizedMessage)
            }
        }
    }

    private fun showInstanceDetails(instance: Instance) {
        screenNavigation.navigate {
            it.copy(
                configurations = it.configurations.filterIndexed { i, _ -> i <= it.index } + ScreenConfig.InstanceDetails(
                    nextScreenId,
                    instance
                ),
                index = it.index + 1
            )
        }
    }

    private fun launchInstance(instance: Instance) {

    }

    private fun showProjectDetails(project: Project) {

    }

    private fun installProject(instance: Project) {

    }

    @Serializable
    sealed interface ScreenConfig {
        @Serializable
        data object None : ScreenConfig

        @Serializable
        data class Home(val screenId: Long) : ScreenConfig

        @Serializable
        data class Instances(val screenId: Long) : ScreenConfig

        @Serializable
        data class Search(val screenId: Long) : ScreenConfig

        @Serializable
        data class Settings(val screenId: Long) : ScreenConfig

        @Serializable
        data class InstanceDetails(val screenId: Long, val instance: Instance) : ScreenConfig
    }

    @Serializable
    sealed interface DialogConfig {
        @Serializable
        data object CreateNewInstance : DialogConfig

        @Serializable
        data object AddAccount : DialogConfig

        @Serializable
        data class Error(val title: String, val message: List<String>) : DialogConfig
    }

    @Serializable
    private data class AppNavigationState(
        val configurations: List<ScreenConfig>,
        val index: Int
    ) : NavState<ScreenConfig> {
        override val children: List<ChildNavState<ScreenConfig>> by lazy {
            configurations.mapIndexed { index, screenConfig ->
                SimpleChildNavState(
                    configuration = screenConfig,
                    status = if (index == this.index) ChildNavState.Status.RESUMED else ChildNavState.Status.CREATED
                )
            }
        }
    }
}

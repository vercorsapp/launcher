package com.skyecodes.vercors.component

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.children.*
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.jthemedetecor.OsThemeDetector
import com.skyecodes.vercors.component.dialog.CreateNewInstanceDialogComponent
import com.skyecodes.vercors.component.dialog.DefaultCreateNewInstanceDialogComponent
import com.skyecodes.vercors.component.dialog.DefaultErrorDialogComponent
import com.skyecodes.vercors.component.dialog.ErrorDialogComponent
import com.skyecodes.vercors.component.screen.*
import com.skyecodes.vercors.data.model.app.AppTab
import com.skyecodes.vercors.data.model.app.AppTheme
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.service.ConfigurationService
import com.skyecodes.vercors.data.service.InstanceService
import com.skyecodes.vercors.ui.UI
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import java.awt.Dimension
import java.awt.Toolkit
import java.util.concurrent.atomic.AtomicLong

private val logger = KotlinLogging.logger { }

interface RootComponent {
    val children: Value<Children<*, ScreenChild>>
    val dialog: Value<ChildStack<*, DialogChild>>
    val uiState: StateFlow<AppUiState>
    val configuration: StateFlow<Configuration?>
    val instances: StateFlow<List<Instance>?>
    val activeTab: StateFlow<AppTab>

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
    fun openErrorDialog(title: String, vararg message: String)
    fun closeDialog()

    sealed class ScreenChild(val tab: AppTab, val isDefault: Boolean) {
        class Home(val component: HomeComponent) : ScreenChild(AppTab.Home, true), Refreshable by component

        class Instances(val component: InstancesComponent) : ScreenChild(AppTab.Instances, true),
            Refreshable by component
        class Search(val component: SearchComponent) : ScreenChild(AppTab.Search, true), Refreshable by component
        class Accounts(val component: AccountsComponent) : ScreenChild(AppTab.Accounts, true)
        class Settings(val component: SettingsComponent) : ScreenChild(AppTab.Settings, true), Refreshable by component
    }

    sealed interface DialogChild {
        data object None : DialogChild
        class CreateNewInstance(val component: CreateNewInstanceDialogComponent) : DialogChild
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

    data class AppUiState(
        val palette: UI.Palette,
        val fatalError: Throwable? = null,
        val error: Throwable? = null
    )
}

interface Refreshable {
    fun refresh()
}

class DefaultRootComponent(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.get(),
    private val instanceService: InstanceService = componentContext.get()
) : AppComponentContext by componentContext, RootComponent {
    private lateinit var window: ComposeWindow
    private lateinit var windowState: WindowState
    private lateinit var savedPos: WindowPosition
    private lateinit var savedWindowMode: WindowPlacement
    private lateinit var savedSize: Dimension
    private lateinit var cancellation: Cancellation
    private val detector = OsThemeDetector.getDetector()
    private val detectorListener: (Boolean) -> Unit =
        { if (configuration.value?.theme === AppTheme.SYSTEM) updatePalette() }
    private val screenId = AtomicLong(0)
    private val screenNavigation = SimpleNavigation<(AppNavigationState) -> AppNavigationState>()

    override val uiState: MutableStateFlow<RootComponent.AppUiState> =
        MutableStateFlow(RootComponent.AppUiState(UI.Mocha))
    override val configuration: MutableStateFlow<Configuration?> = MutableStateFlow(null)
    override val instances: MutableStateFlow<List<Instance>?> = MutableStateFlow(null)
    override val activeTab: MutableStateFlow<AppTab> = MutableStateFlow(Configuration.DEFAULT.defaultTab)
    override val children: Value<RootComponent.Children<ScreenConfig, RootComponent.ScreenChild>> = children(
        source = screenNavigation,
        stateSerializer = AppNavigationState.serializer(),
        key = "screenNavigation",
        initialState = {
            AppNavigationState(
                configurations = listOf(ScreenConfig.Home(screenId.getAndIncrement())),
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
    private val dialogNavigation = StackNavigation<DialogConfig>()
    override val dialog: Value<ChildStack<*, RootComponent.DialogChild>> = appChildStack(
        source = dialogNavigation,
        serializer = DialogConfig.serializer(),
        initialConfiguration = DialogConfig.None,
        key = "dialogNavigation",
        handleBackButton = true,
        childFactory = ::createDialog
    )

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
        with(scope) {
            launch(handler) { instances.value = instanceService.loadInstances() }
            launch(handler) { configuration.filterNotNull().collect { updatePalette() } }
            launch(handler) {
                configurationService.load().let {
                    configuration.value = it
                    initNavigation(it.defaultTab)
                }
            }
        }
        detector.registerListener(detectorListener)
        cancellation = children.observe {
            logger.info { "Navigated to ${it.active.instance.tab.title}" }
            activeTab.value = it.active.instance.tab
        }
    }

    private fun onDestroy() {
        detector.removeListener(detectorListener)
        cancellation.cancel()
    }

    private fun updatePalette() {
        val palette = when (configuration.value?.theme) {
            AppTheme.DARK -> UI.Mocha
            AppTheme.LIGHT -> UI.Latte
            else -> if (detector.isDark) UI.Mocha else UI.Latte
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
    }

    private fun createDefaultConfigForTab(tab: AppTab) = when (tab) {
        AppTab.Home -> ScreenConfig.Home(screenId.getAndIncrement())
        AppTab.Instances -> ScreenConfig.Instances(screenId.getAndIncrement())
        AppTab.Search -> ScreenConfig.Search(screenId.getAndIncrement())
        AppTab.Accounts -> ScreenConfig.Accounts(screenId.getAndIncrement())
        AppTab.Settings -> ScreenConfig.Settings(screenId.getAndIncrement())
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
        dialogNavigation.replaceCurrent(DialogConfig.CreateNewInstance)
    }

    override fun openErrorDialog(title: String, vararg message: String) {
        dialogNavigation.replaceCurrent(DialogConfig.Error(title, message.toList()))
    }

    override fun closeDialog() {
        dialogNavigation.replaceCurrent(DialogConfig.None)
    }

    override fun updateConfiguration(config: Configuration) {
        configuration.update { config }
        scope.launch { configurationService.save(config) }
    }

    private fun createChild(config: ScreenConfig, componentContext: AppComponentContext): RootComponent.ScreenChild =
        when (config) {
            is ScreenConfig.Accounts -> RootComponent.ScreenChild.Accounts(accountsComponent(componentContext))
            is ScreenConfig.Home -> RootComponent.ScreenChild.Home(homeComponent(componentContext))
            is ScreenConfig.Instances -> RootComponent.ScreenChild.Instances(instancesComponent(componentContext))
            is ScreenConfig.Search -> RootComponent.ScreenChild.Search(searchComponent(componentContext))
            is ScreenConfig.Settings -> RootComponent.ScreenChild.Settings(settingsComponent(componentContext))
        }

    private fun accountsComponent(componentContext: AppComponentContext): AccountsComponent = DefaultAccountsComponent(
        componentContext = componentContext
    )

    private fun homeComponent(componentContext: AppComponentContext): HomeComponent = DefaultHomeComponent(
        componentContext = componentContext,
        configuration = configuration,
        instances = instances
    )

    private fun instancesComponent(componentContext: AppComponentContext): InstancesComponent =
        DefaultInstancesComponent(
            componentContext = componentContext,
            instances = instances,
            openNewInstanceDialog = ::openNewInstanceDialog
        )

    private fun searchComponent(componentContext: AppComponentContext): SearchComponent = DefaultSearchComponent(
        componentContext = componentContext
    )

    private fun settingsComponent(componentContext: AppComponentContext): SettingsComponent = DefaultSettingsComponent(
        componentContext = componentContext,
        onConfigurationChange = ::updateConfiguration
    )

    private fun createDialog(config: DialogConfig, componentContext: AppComponentContext): RootComponent.DialogChild =
        when (config) {
            is DialogConfig.None -> RootComponent.DialogChild.None
            is DialogConfig.CreateNewInstance -> RootComponent.DialogChild.CreateNewInstance(
                createNewInstanceComponent(
                    componentContext
                )
            )
            is DialogConfig.Error -> RootComponent.DialogChild.Error(createErrorComponent(componentContext, config))
        }

    private fun createNewInstanceComponent(componentContext: AppComponentContext): CreateNewInstanceDialogComponent =
        DefaultCreateNewInstanceDialogComponent(
            componentContext = componentContext,
            onCreateInstance = ::onCreateInstance,
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
                instanceService.createInstance(instance)
                logger.info { "Created instance ${instance.name}" }
                instances.update { it?.plus(instance) ?: listOf(instance) }
            } catch (e: Exception) {
                val title = "An error occured while creating instance"
                logger.error(e) { title }
                openErrorDialog(title, "Please check the logs for more details.", e.localizedMessage)
            }
        }
    }

    @Serializable
    sealed interface ScreenConfig {
        @Serializable
        data class Home(val screenId: Long) : ScreenConfig
        @Serializable
        data class Instances(val screenId: Long) : ScreenConfig
        @Serializable
        data class Search(val screenId: Long) : ScreenConfig
        @Serializable
        data class Accounts(val screenId: Long) : ScreenConfig
        @Serializable
        data class Settings(val screenId: Long) : ScreenConfig
    }

    @Serializable
    sealed interface DialogConfig {
        @Serializable
        data object None : DialogConfig
        @Serializable
        data object CreateNewInstance : DialogConfig

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
                    status = if (index == this.index) ChildNavState.Status.ACTIVE else ChildNavState.Status.INACTIVE
                )
            }
        }
    }
}

private inline fun <reified C : Any, T : Any> AppComponentContext.appChildStack(
    source: StackNavigationSource<C>,
    serializer: KSerializer<C>?,
    initialConfiguration: C,
    key: String = "DefaultStack",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, AppComponentContext) -> T
): Value<ChildStack<C, T>> =
    childStack(
        source = source,
        serializer = serializer,
        initialConfiguration = initialConfiguration,
        key = key,
        handleBackButton = handleBackButton
    ) { configuration, componentContext ->
        childFactory(
            configuration,
            DefaultAppComponentContext(
                componentContext = componentContext,
                koin = koin
            )
        )
    }
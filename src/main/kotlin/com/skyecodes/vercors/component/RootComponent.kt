package com.skyecodes.vercors.component

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.children.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.jthemedetecor.OsThemeDetector
import com.skyecodes.vercors.data.model.app.AppTab
import com.skyecodes.vercors.data.model.app.AppTheme
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.service.ConfigurationService
import com.skyecodes.vercors.data.service.InstanceService
import com.skyecodes.vercors.ui.UI
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.awt.Dimension
import java.awt.Toolkit
import java.util.concurrent.atomic.AtomicLong

private val logger = KotlinLogging.logger { }

interface Refreshable {
    fun refresh()
}

interface RootComponent {
    val children: Value<Children<*, NavChild>>
    val uiState: StateFlow<AppUiState>
    val configuration: StateFlow<Configuration?>
    val instances: StateFlow<List<Instance>?>
    val currentTab: StateFlow<AppTab>

    fun initializeWindowState(windowState: WindowState)
    fun initializeWindow(window: ComposeWindow)
    fun onMinimize()
    fun onMaximize()
    fun navigate(tab: AppTab)

    //fun updateConfiguration(config: Configuration)
    fun onNextScreen()
    fun onPreviousScreen()
    fun onRefreshScreen()
    fun openNewInstanceDialog()
    fun closeNewInstanceDialog()

    sealed interface NavChild {
        val isDefault: Boolean
        val tab: AppTab

        class Home(val component: HomeComponent) : NavChild, Refreshable by component {
            override val tab = AppTab.Home
            override val isDefault = true
        }

        class Instances(val component: InstancesComponent) : NavChild, Refreshable by component {
            override val tab = AppTab.Instances
            override val isDefault = true
        }

        class Search(val component: SearchComponent) : NavChild, Refreshable by component {
            override val tab = AppTab.Search
            override val isDefault = true
        }

        class Accounts(val component: AccountsComponent) : NavChild {
            override val tab = AppTab.Accounts
            override val isDefault = true
        }

        class Settings(val component: SettingsComponent) : NavChild, Refreshable by component {
            override val tab = AppTab.Settings
            override val isDefault = true
        }
    }

    class Children<out C : Any, out T : Any>(
        val items: List<Child.Created<C, T>>,
        val index: Int
    ) {
        val current: Child.Created<C, T> = items[index]
        val hasPreviousScreen = index > 0
        val hasNextScreen = index < items.size - 1
        val canRefreshScreen = current.instance is Refreshable
    }
}

class DefaultRootComponent(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.koin.get(),
    private val instanceService: InstanceService = componentContext.koin.get()
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
    private val navigation = SimpleNavigation<(AppNavigationState) -> AppNavigationState>()

    override val uiState: MutableStateFlow<AppUiState> = MutableStateFlow(AppUiState(UI.Mocha))
    override val configuration: MutableStateFlow<Configuration?> = MutableStateFlow(null)
    override val instances: MutableStateFlow<List<Instance>?> = MutableStateFlow(null)
    override val currentTab: MutableStateFlow<AppTab> = MutableStateFlow(Configuration.DEFAULT.defaultTab)
    override val children: Value<RootComponent.Children<NavConfig, RootComponent.NavChild>> = children(
        source = navigation,
        stateSerializer = AppNavigationState.serializer(),
        key = "rootNavigation",
        initialState = {
            AppNavigationState(
                configurations = listOf(NavConfig.Home(screenId.getAndIncrement())),
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

    private val currentChild get() = children.value.current.instance

    init {
        lifecycle.doOnCreate(::onCreate)
        lifecycle.doOnDestroy(::onDestroy)
    }

    private fun onCreate() {
        logger.info { "Creating RootComponent" }
        with(scope) {
            launch { instances.value = instanceService.loadInstances(this) }
            launch { configuration.filterNotNull().collect { updatePalette() } }
            launch {
                configurationService.load().let {
                    configuration.value = it
                    initNavigation(it.defaultTab)
                }
            }
        }
        detector.registerListener(detectorListener)
        cancellation = children.observe {
            logger.info { "Navigated to ${it.current.instance.tab.title}" }
            currentTab.value = it.current.instance.tab
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
        navigation.navigate {
            it.copy(
                configurations = listOf(createDefaultConfigForTab(tab)),
                index = 0
            )
        }
    }

    override fun navigate(tab: AppTab) {
        if (currentTab.value === tab && currentChild.isDefault) return
        navigation.navigate {
            it.copy(
                configurations = it.configurations.filterIndexed { i, _ -> i <= it.index } + createDefaultConfigForTab(
                    tab
                ),
                index = it.index + 1
            )
        }
    }

    private fun createDefaultConfigForTab(tab: AppTab) = when (tab) {
        AppTab.Home -> NavConfig.Home(screenId.getAndIncrement())
        AppTab.Instances -> NavConfig.Instances(screenId.getAndIncrement())
        AppTab.Search -> NavConfig.Search(screenId.getAndIncrement())
        AppTab.Accounts -> NavConfig.Accounts(screenId.getAndIncrement())
        AppTab.Settings -> NavConfig.Settings(screenId.getAndIncrement())
    }

    override fun onNextScreen() {
        if (children.value.index < children.value.items.size - 1) {
            logger.info { "Navigating to next screen..." }
            navigation.navigate { it.copy(index = it.index + 1) }
        }
    }

    override fun onPreviousScreen() {
        if (children.value.index > 0) {
            logger.info { "Navigating to previous screen..." }
            navigation.navigate { it.copy(index = it.index - 1) }
        }
    }

    override fun onRefreshScreen() {
        logger.info { "Refreshing screen" }
        currentChild.let {
            if (it is Refreshable) it.refresh()
        }
    }

    override fun openNewInstanceDialog() = updateNewInstanceDialog(true)

    override fun closeNewInstanceDialog() = updateNewInstanceDialog(false)

    private fun updateConfiguration(config: Configuration) {
        configuration.update { config }
        scope.launch { configurationService.save(config) }
    }

    private fun updateNewInstanceDialog(showNewInstanceDialog: Boolean) {
        uiState.update { it.copy(showNewInstanceDialog = showNewInstanceDialog) }
    }

    private fun createChild(config: NavConfig, componentContext: AppComponentContext): RootComponent.NavChild =
        when (config) {
            is NavConfig.Accounts -> RootComponent.NavChild.Accounts(accountsComponent(componentContext))
            is NavConfig.Home -> RootComponent.NavChild.Home(homeComponent(componentContext))
            is NavConfig.Instances -> RootComponent.NavChild.Instances(instancesComponent(componentContext))
            is NavConfig.Search -> RootComponent.NavChild.Search(searchComponent(componentContext))
            is NavConfig.Settings -> RootComponent.NavChild.Settings(settingsComponent(componentContext))
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

    @Serializable
    sealed interface NavConfig {
        @Serializable
        data class Home(val screenId: Long) : NavConfig

        @Serializable
        data class Instances(val screenId: Long) : NavConfig

        @Serializable
        data class Search(val screenId: Long) : NavConfig

        @Serializable
        data class Accounts(val screenId: Long) : NavConfig

        @Serializable
        data class Settings(val screenId: Long) : NavConfig
    }

    @Serializable
    private data class AppNavigationState(
        val configurations: List<NavConfig>,
        val index: Int
    ) : NavState<NavConfig> {
        override val children: List<ChildNavState<NavConfig>> by lazy {
            configurations.mapIndexed { index, navConfig ->
                SimpleChildNavState(
                    configuration = navConfig,
                    status = if (index == this.index) ChildNavState.Status.ACTIVE else ChildNavState.Status.INACTIVE
                )
            }
        }
    }
}

data class AppUiState(
    val palette: UI.Palette,
    val showNewInstanceDialog: Boolean = false
)

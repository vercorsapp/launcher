package com.skyecodes.vercors.component

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.jthemedetecor.OsThemeDetector
import com.skyecodes.vercors.data.model.app.AppScene
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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import java.awt.Dimension
import java.awt.Toolkit

private val logger = KotlinLogging.logger { }

interface RootComponent {
    val childStack: Value<ChildStack<*, NavChild>>
    val uiState: StateFlow<AppUiState>
    val configuration: StateFlow<Configuration?>
    val instances: StateFlow<List<Instance>?>
    val currentScene: StateFlow<AppScene>

    fun initializeWindowState(windowState: WindowState)
    fun initializeWindow(window: ComposeWindow)
    fun onMinimize()
    fun onMaximize()
    fun navigate(scene: AppScene)
    fun updateConfiguration(config: Configuration)
    fun onNextScene()
    fun onPreviousScene()
    fun onRefresh()
    fun openNewInstanceDialog()
    fun closeNewInstanceDialog()

    sealed interface NavChild {
        val scene: AppScene

        class Home(val component: HomeComponent) : NavChild {
            override val scene = AppScene.Home
        }

        class Instances(val component: InstancesComponent) : NavChild {
            override val scene = AppScene.Home
        }

        class Search(val component: SearchComponent) : NavChild {
            override val scene = AppScene.Home
        }

        class Accounts(val component: AccountsComponent) : NavChild {
            override val scene = AppScene.Home
        }

        class Settings(val component: SettingsComponent) : NavChild {
            override val scene = AppScene.Home
        }
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
    private val detector = OsThemeDetector.getDetector()
    private val detectorListener: (Boolean) -> Unit =
        { if (configuration.value?.theme === AppTheme.SYSTEM) updatePalette() }
    private val navigation = StackNavigation<NavConfig>()

    override val uiState: MutableStateFlow<AppUiState> = MutableStateFlow(AppUiState(UI.Mocha, false))
    override val configuration: MutableStateFlow<Configuration?> = MutableStateFlow(null)
    override val instances: MutableStateFlow<List<Instance>?> = MutableStateFlow(null)
    override val currentScene: MutableStateFlow<AppScene> = MutableStateFlow(Configuration.DEFAULT.defaultScene)
    override val childStack = appChildStack(
        source = navigation,
        serializer = NavConfig.serializer(),
        initialConfiguration = NavConfig.Home,
        childFactory = ::createChild
    )

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
                    currentScene.value = it.defaultScene
                }
            }
        }
        detector.registerListener(detectorListener)
    }

    private fun onDestroy() {
        detector.removeListener(detectorListener)
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

    override fun navigate(scene: AppScene) {
        navigation.bringToFront(
            when (scene) {
                AppScene.Home -> NavConfig.Home
                AppScene.Instances -> NavConfig.Instances
                AppScene.Search -> NavConfig.Search
                AppScene.Accounts -> NavConfig.Accounts
                AppScene.Settings -> NavConfig.Settings
            }
        )
        currentScene.value = scene
    }

    override fun updateConfiguration(config: Configuration) {
        configuration.update { config }
        scope.launch { configurationService.save(config) }
    }

    override fun onNextScene() {
        logger.info { "Navigating to next scene" }
    }

    override fun onPreviousScene() {
        logger.info { "Navigating to previous scene" }
        navigation.pop()
        updateScene(childStack.active.instance.scene)
    }

    private fun updateScene(scene: AppScene) {
        currentScene.value = scene
    }

    override fun onRefresh() {
        logger.info { "Refreshing scene" }
    }

    override fun openNewInstanceDialog() = updateNewInstanceDialog(true)

    override fun closeNewInstanceDialog() = updateNewInstanceDialog(false)

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
        onConfigurationChange = { configuration.value = it }
    )

    @Serializable
    sealed interface NavConfig {
        @Serializable
        data object Home : NavConfig

        @Serializable
        data object Instances : NavConfig

        @Serializable
        data object Search : NavConfig

        @Serializable
        data object Accounts : NavConfig

        @Serializable
        data object Settings : NavConfig
    }
}

data class AppUiState(val palette: UI.Palette, val showNewInstanceDialog: Boolean)

inline fun <reified C : Any, T : Any> AppComponentContext.appChildStack(
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
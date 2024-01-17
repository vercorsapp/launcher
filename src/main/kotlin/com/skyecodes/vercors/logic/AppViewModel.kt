package com.skyecodes.vercors.logic

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import com.jthemedetecor.OsThemeDetector
import com.skyecodes.vercors.data.model.app.AppScene
import com.skyecodes.vercors.data.model.app.AppTheme
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.service.ConfigurationService
import com.skyecodes.vercors.data.service.InstanceService
import com.skyecodes.vercors.ui.UI
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.awt.Dimension
import java.awt.Toolkit

private val logger = KotlinLogging.logger { }

class AppViewModel(
    private val configurationService: ConfigurationService,
    private val instanceService: InstanceService,
    val navigator: Navigator
) : ViewModel() {
    private var isInitialized = false
    private var isWindowInitialized = false
    private lateinit var window: ComposeWindow
    lateinit var windowState: WindowState
    private lateinit var savedPos: WindowPosition
    private lateinit var savedWindowMode: WindowPlacement
    private lateinit var savedSize: Dimension
    private val detector = OsThemeDetector.getDetector()

    private val _uiState = MutableStateFlow(AppUiState(UI.Mocha))
    val uiState = _uiState.asStateFlow()
    private val _configuration = MutableStateFlow<Configuration?>(null)
    val configuration = _configuration.asStateFlow()
    private val _instances = MutableStateFlow<List<Instance>?>(null)
    val instances = _instances.asStateFlow()
    private val _currentScene = MutableStateFlow(Configuration.DEFAULT.defaultScene)
    val currentScene = _currentScene.asStateFlow()
    private val sceneStack = ArrayDeque<AppScene>()
    private lateinit var canNavigate: StateFlow<Boolean>
    private lateinit var canGoBack: StateFlow<Boolean>

    fun initialize(windowState: WindowState) {
        if (isInitialized) return
        isInitialized = true
        logger.info { "Initializing app" }
        this.windowState = windowState
        viewModelScope.launch {
            configuration.filterNotNull().collect { config ->
                val palette = when (config.theme) {
                    AppTheme.DARK -> UI.Mocha
                    AppTheme.LIGHT -> UI.Latte
                    else -> getPalette(detector.isDark)
                }
                _uiState.update { uiState -> uiState.copy(palette = palette) }
            }
        }
        viewModelScope.launch {
            _configuration.update { configurationService.load() }
            configuration.value?.defaultScene?.let { updateScene(it) }
        }
        viewModelScope.launch {
            _instances.update { (instanceService.loadInstances(this)) }
        }
        viewModelScope.launch {
            canNavigate = navigator.canNavigate.stateIn(this)
            navigator.currentEntry.stateIn(this)
        }
        viewModelScope.launch {
            canGoBack = navigator.canGoBack.stateIn(this)
        }
        detector.registerListener {
            if (configuration.value?.theme === AppTheme.SYSTEM) {
                val newPalette = getPalette(it)
                if (uiState.value.palette != newPalette) {
                    _uiState.update { uiState -> uiState.copy(palette = newPalette) }
                }
            }
        }
    }

    private fun getPalette(isDark: Boolean): UI.Palette = if (isDark) UI.Mocha else UI.Latte

    fun initializeWindow(window: ComposeWindow) {
        if (isWindowInitialized && this.window == window) return
        isWindowInitialized = true
        logger.info { "Initializing window" }
        this.window = window
        window.minimumSize = Dimension(1024, 576)
        savedWindowMode = window.placement
        savedSize = window.size
        savedPos = windowState.position
    }

    fun onMinimize() {
        windowState.isMinimized = true
    }

    fun onMaximize() {
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

    fun navigate(scene: AppScene) {
        if (canNavigate.value && currentScene.value != scene) {
            logger.info { "Navigating to ${scene.route}" }
            navigator.navigate(scene.route)
            updateScene(scene)
        }
    }

    fun updateConfiguration(config: Configuration) {
        _configuration.update { config }
        viewModelScope.launch { configurationService.save(config) }
    }

    fun onNextScene() {
        logger.info { "Navigating to next scene" }
    }

    fun onPreviousScene() {
        logger.info { "Navigating to previous scene" }
        if (canGoBack.value) {
            navigator.goBack()
            sceneStack.removeLast()
            _currentScene.update { sceneStack.last() }
        }
    }

    private fun updateScene(scene: AppScene) {
        _currentScene.update { scene }
        sceneStack += scene
    }

    fun onRefresh() {
        logger.info { "Refreshing scene" }
    }
}

data class AppUiState(val palette: UI.Palette)
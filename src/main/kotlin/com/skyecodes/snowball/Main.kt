package com.skyecodes.snowball

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.skyecodes.snowball.data.app.Configuration
import com.skyecodes.snowball.service.ConfigurationService
import com.skyecodes.snowball.ui.App
import com.skyecodes.snowball.ui.UI
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.Toolkit

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Unnamed Launcher"

private val logger = KotlinLogging.logger {}

fun main() = application {
    logger.info { "Hello world! $APP_NAME v$APP_VERSION" }
    var configuration by remember { mutableStateOf(Configuration.DEFAULT) }
    val state = rememberWindowState(size = DpSize(1080.dp, 720.dp))

    LaunchedEffect(true) {
        configuration = ConfigurationService.load()
    }

    Window(
        state = state,
        onCloseRequest = ::exit,
        undecorated = true,
        title = APP_NAME
    ) {
        val mode = remember { mutableStateOf(window.placement) }
        val size = remember { mutableStateOf(window.size) }
        val pos = remember { mutableStateOf(state.position) }
        var colors by remember { mutableStateOf(UI.colors.material) }
        val isSystemDarkTheme = isSystemInDarkTheme()

        LaunchedEffect(configuration) {
            UI.colors = when (configuration.theme) {
                Configuration.Theme.DARK -> UI.Mocha
                Configuration.Theme.LIGHT -> UI.Latte
                else -> if (isSystemDarkTheme) UI.Mocha else UI.Latte
            }
            colors = UI.colors.material
        }

        App(
            configuration,
            colors,
            onMinimize = { state.isMinimized = true },
            onMaximize = {
                mode.value = if (mode.value == WindowPlacement.Floating) {
                    size.value = window.size
                    pos.value = state.position
                    val insets = Toolkit.getDefaultToolkit().getScreenInsets(window.graphicsConfiguration)
                    val screenBounds = window.graphicsConfiguration.bounds
                    window.setSize(
                        screenBounds.width - (insets.left + insets.right),
                        screenBounds.height - (insets.top + insets.bottom)
                    )
                    window.setLocation(screenBounds.x + insets.left, screenBounds.y + insets.top)
                    WindowPlacement.Maximized
                } else {
                    window.size = size.value
                    state.position = pos.value
                    WindowPlacement.Floating
                }
            },
            onClose = ::exit
        )
    }
}

private fun ApplicationScope.exit() {
    exitApplication()
    logger.info { "Goodbye!" }
}

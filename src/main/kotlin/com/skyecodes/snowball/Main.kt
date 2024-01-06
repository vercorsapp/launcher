package com.skyecodes.snowball

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.skyecodes.snowball.ui.App
import java.awt.Toolkit

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Snowball Launcher"

fun main() = application {
    val state = rememberWindowState(size = DpSize(1080.dp, 720.dp))

    Window(
        state = state,
        onCloseRequest = ::exitApplication,
        undecorated = true,
        title = APP_NAME
    ) {
        val mode = remember { mutableStateOf(window.placement) }
        val size = remember { mutableStateOf(window.size) }
        val pos = remember { mutableStateOf(state.position) }

        App(
            onMinimize = { state.isMinimized = true },
            onMaximize = {
                mode.value = if (mode.value == WindowPlacement.Floating) {
                    size.value = window.size
                    pos.value = state.position
                    val insets = Toolkit.getDefaultToolkit().getScreenInsets(window.graphicsConfiguration)
                    val bounds = window.graphicsConfiguration.bounds
                    window.setSize(bounds.width, bounds.height - insets.bottom)
                    window.setLocation(0, 0)
                    WindowPlacement.Maximized
                } else {
                    window.size = size.value
                    state.position = pos.value
                    WindowPlacement.Floating
                }
            },
            onClose = ::exitApplication
        )
    }
}

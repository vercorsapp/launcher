package com.skyecodes.snowball

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.skyecodes.snowball.ui.App

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Snowball Launcher"

fun main() = application {
    val state = rememberWindowState()

    Window(
        state = state,
        onCloseRequest = ::exitApplication,
        undecorated = true,
        title = APP_NAME
    ) {
        App(
            onMinimize = { state.isMinimized = true },
            onMaximize = { state.placement = if (state.placement == WindowPlacement.Maximized) WindowPlacement.Floating else WindowPlacement.Maximized },
            onClose = ::exitApplication
        )
    }
}

package com.skyecodes.snowball

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.skyecodes.snowball.ui.App

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Snowball Launcher"

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = APP_NAME) {
        App()
    }
}

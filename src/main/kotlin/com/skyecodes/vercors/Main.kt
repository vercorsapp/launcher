package com.skyecodes.vercors

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.skyecodes.vercors.service.*
import com.skyecodes.vercors.service.impl.*
import com.skyecodes.vercors.ui.App
import io.github.oshai.kotlinlogging.KotlinLogging
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger
import java.awt.Toolkit

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Vercors"

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello world! $APP_NAME v$APP_VERSION" }
    application {
        PreComposeApp {
            val state = rememberWindowState(size = DpSize(1080.dp, 720.dp))

            Window(
                state = state,
                onCloseRequest = ::exit,
                undecorated = true,
                title = APP_NAME
            ) {
                val mode = remember { mutableStateOf(window.placement) }
                val size = remember { mutableStateOf(window.size) }
                val pos = remember { mutableStateOf(state.position) }

                KoinApplication(application = {
                    logger(SLF4JLogger())
                    modules(module {
                        single<ConfigurationService> { ConfigurationServiceImpl(get<StorageService>()) }
                        single<CurseforgeService> { CurseforgeServiceImpl(get<HttpService>()) }
                        single<HttpService> { HttpServiceImpl() }
                        single<InstanceService> { InstanceServiceImpl(get<StorageService>()) }
                        single<ModrinthService> { ModrinthServiceImpl(get<HttpService>()) }
                        single<MojangService> { MojangServiceImpl(get<HttpService>()) }
                        single<StorageService> { StorageServiceImpl() }
                    })
                }) {
                    App(
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
        }
    }
}

private fun ApplicationScope.exit() {
    exitApplication()
    logger.info { "Goodbye!" }
}

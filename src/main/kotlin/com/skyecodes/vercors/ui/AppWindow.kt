package com.skyecodes.vercors.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.skyecodes.vercors.APP_NAME
import com.skyecodes.vercors.component.RootComponent
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.service.ConfigurationState
import com.skyecodes.vercors.data.service.InstancesState
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun ApplicationScope.AppWindow(
    component: RootComponent,
    lifecycle: LifecycleRegistry
) {
    val windowState = rememberWindowState(size = DpSize(1280.dp, 720.dp))
    LifecycleController(lifecycle, windowState)
    LaunchedEffect(component) {
        component.initializeWindowState(windowState)
    }

    val configurationState by component.configurationState.collectAsState()
    val instancesState by component.instancesState.collectAsState()
    val uiState by component.uiState.collectAsState()

    val fatalError = uiState.fatalError
    if (fatalError != null) {
        CompositionLocalProvider(
            LocalPalette provides Palette.Mocha,
            LocalConfiguration provides Configuration.DEFAULT
        ) {
            MaterialTheme(
                colors = LocalPalette.current.material(LocalConfiguration.current.accentColor.ofPalette(LocalPalette.current)),
                typography = UI.typography
            ) {
                DialogWindow(
                    onCloseRequest = ::onClose,
                    title = APP_NAME
                ) {
                    Surface {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                            modifier = Modifier.fillMaxSize().padding(UI.largePadding)
                        ) {
                            Text("An error occured while loading Vercors.", style = MaterialTheme.typography.h6)
                            Text(fatalError.localizedMessage)
                        }
                    }
                }
            }
        }
    } else {
        val finalConfigurationState = configurationState
        val finalInstancesState = instancesState
        if (finalConfigurationState is ConfigurationState.Loaded && finalInstancesState is InstancesState.Loaded)
            if (finalConfigurationState.config.useSystemWindowFrame) {
                Window(
                    state = windowState,
                    onCloseRequest = ::onClose,
                    undecorated = false,
                    title = APP_NAME,
                ) {
                    AppContent(component, finalConfigurationState.config, uiState, ::onClose)
                }
            } else {
                Window(
                    state = windowState,
                    onCloseRequest = ::onClose,
                    undecorated = true,
                    title = APP_NAME,
                ) {
                    AppContent(component, finalConfigurationState.config, uiState, ::onClose)
                }
            }
    }
}

private fun ApplicationScope.onClose() {
    logger.info { "Exiting application" }
    exitApplication()
}
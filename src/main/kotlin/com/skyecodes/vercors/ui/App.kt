package com.skyecodes.vercors.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import com.skyecodes.vercors.data.app.AppScene
import com.skyecodes.vercors.data.app.Configuration
import com.skyecodes.vercors.data.app.Instance
import com.skyecodes.vercors.service.ConfigurationService
import com.skyecodes.vercors.service.InstanceService
import com.skyecodes.vercors.ui.accounts.AccountsContent
import com.skyecodes.vercors.ui.home.HomeContent
import com.skyecodes.vercors.ui.instances.InstancesContent
import com.skyecodes.vercors.ui.search.SearchContent
import com.skyecodes.vercors.ui.settings.SettingsContent
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WindowScope.App(
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit
) {
    var configuration by remember { mutableStateOf(Configuration.DEFAULT) }
    var instances by remember { mutableStateOf(emptyList<Instance>()) }
    val configurationService = koinInject<ConfigurationService>()
    val instanceService = koinInject<InstanceService>()
    var colors by remember { mutableStateOf(UI.colors.material) }
    var currentScene by remember { mutableStateOf(configuration.defaultScene) }
    val isSystemDarkTheme = isSystemInDarkTheme()
    val navigator = rememberNavigator()

    LaunchedEffect(true) {
        configuration = configurationService.load()
    }

    LaunchedEffect(true) {
        instances = instanceService.loadInstances(this)
    }

    LaunchedEffect(configuration) {
        UI.colors = when (configuration.theme) {
            Configuration.Theme.DARK -> UI.Mocha
            Configuration.Theme.LIGHT -> UI.Latte
            else -> if (isSystemDarkTheme) UI.Mocha else UI.Latte
        }
        colors = UI.colors.material
    }

    MaterialTheme(
        colors = colors,
        typography = UI.typography,
        //shapes = UI.shapes
    ) {
        Row {
            Surface(
                modifier = Modifier.fillMaxHeight().background(color = UI.colors.surface0).shadow(4.dp)
            ) {
                Menu(currentScene) {
                    currentScene = it
                    navigator.navigate(it.route)
                }
            }
            Column {
                WindowDraggableArea(
                    modifier = Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                        onDoubleClick = onMaximize,
                        onLongClick = null
                    )
                ) {
                    Surface(
                        modifier = Modifier.height(40.dp).fillMaxWidth()
                            .background(color = UI.colors.surface0)
                    ) {
                        Toolbar(currentScene, onMinimize, onMaximize, onClose)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = UI.colors.base
                ) {
                    NavHost(
                        navigator = navigator,
                        initialRoute = configuration.defaultScene.route,
                        navTransition = NavTransition(
                            createTransition = fadeIn(),
                            destroyTransition = fadeOut(),
                            pauseTransition = fadeOut(),
                            resumeTransition = fadeIn()
                        )
                    ) {
                        scene(AppScene.Home.route) { HomeContent() }
                        scene(AppScene.Instances.route) { InstancesContent() }
                        scene(AppScene.Search.route) { SearchContent() }
                        scene(AppScene.Accounts.route) { AccountsContent() }
                        scene(AppScene.Settings.route) { SettingsContent() }
                    }
                }
            }
        }
    }
}

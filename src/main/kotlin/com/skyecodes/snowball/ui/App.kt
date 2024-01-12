package com.skyecodes.snowball.ui

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
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.skyecodes.snowball.data.app.Configuration
import com.skyecodes.snowball.data.app.Instance
import com.skyecodes.snowball.service.ConfigurationService
import com.skyecodes.snowball.service.InstanceService
import com.skyecodes.snowball.ui.home.HomeTab
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WindowScope.App(
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit
) {
    var configuration by remember { mutableStateOf(Configuration.DEFAULT) }
    var instances by remember { mutableStateOf(emptyList<Instance>()) }
    val configurationService: ConfigurationService by rememberDI { instance() }
    val instanceService: InstanceService by rememberDI { instance() }
    var colors by remember { mutableStateOf(UI.colors.material) }
    val isSystemDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(true) {
        configuration = configurationService.load()
    }

    LaunchedEffect(true) {
        instances = instanceService.fetchInstances(this)
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
        TabNavigator(HomeTab) {
            Row {
                Surface(
                    modifier = Modifier.fillMaxHeight().background(color = UI.colors.surface0).shadow(4.dp)
                ) {
                    Menu()
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
                            Toolbar(onMinimize, onMaximize, onClose)
                        }
                    }
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = UI.colors.base
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}

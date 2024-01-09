package com.skyecodes.snowball.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.skyecodes.snowball.data.app.Configuration
import com.skyecodes.snowball.ui.home.HomeTab

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WindowScope.App(
    configuration: Configuration,
    colors: Colors,
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit
) {
    MaterialTheme(
        colors = colors,
        typography = UI.typography,
        shapes = UI.shapes
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

package app.vercors.root

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import app.vercors.LocalImageLoader
import app.vercors.LocalPalette
import app.vercors.account.AccountListContent
import app.vercors.configuration.ConfigurationData
import app.vercors.dialog.DialogContent
import app.vercors.menu.MenuContent
import app.vercors.navigation.NavigationContent
import app.vercors.notification.NotificationListContent
import app.vercors.root.main.MainComponent
import app.vercors.root.main.MainUiState
import app.vercors.root.main.MainWindowEvent
import app.vercors.toolbar.ToolbarContent
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.disk.DiskCache
import okio.Path.Companion.toOkioPath
import java.awt.Dimension
import java.awt.Toolkit

@Composable
fun MainContent(component: MainComponent, windowState: WindowState, onClose: () -> Unit) {
    val uiState = component.uiState.collectAsState().value

    if (uiState is MainUiState.Loaded) {
        MainAppWindow(
            config = uiState.config,
            windowState = windowState,
            onClose = onClose
        ) {
            val composeWindow = window as ComposeWindow

            LaunchedEffect(composeWindow) {
                composeWindow.minimumSize = Dimension(1280, 720)
            }

            var currentMode by remember { mutableStateOf(composeWindow.placement) }
            var savedSize by remember { mutableStateOf(composeWindow.size) }
            var savedPos by remember { mutableStateOf(windowState.position) }

            uiState.windowEvent?.let {
                LaunchedEffect(it) {
                    when (it) {
                        MainWindowEvent.Minimize -> windowState.isMinimized = true
                        MainWindowEvent.Maximize -> {
                            currentMode = if (currentMode == WindowPlacement.Floating) {
                                savedSize = composeWindow.size
                                savedPos = windowState.position
                                val insets = Toolkit.getDefaultToolkit().getScreenInsets(window.graphicsConfiguration)
                                val bounds = composeWindow.graphicsConfiguration.bounds
                                composeWindow.setSize(
                                    bounds.width - (insets.left + insets.right),
                                    bounds.height - (insets.top + insets.bottom)
                                )
                                composeWindow.setLocation(bounds.x + insets.left, bounds.y + insets.top)
                                WindowPlacement.Maximized
                            } else {
                                composeWindow.size = savedSize
                                windowState.position = savedPos
                                WindowPlacement.Floating
                            }
                        }

                        MainWindowEvent.Close -> onClose()
                    }
                    component.windowEventProcessed()
                }
            }

            AppWindowContent(
                config = uiState.config,
                palette = uiState.palette
            ) {
                CompositionLocalProvider(
                    LocalImageLoader provides ImageLoader.Builder(LocalPlatformContext.current)
                        .diskCache {
                            DiskCache.Builder()
                                .directory(uiState.cachePath.toOkioPath())
                                .build()
                        }
                        .build()
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Row {
                            Surface(
                                modifier = Modifier.fillMaxHeight().background(color = LocalPalette.current.surface0)
                                    .shadow(4.dp)
                            ) {
                                MenuContent(component.menuComponent)
                            }
                            Column {
                                WindowDraggableArea(
                                    modifier = Modifier.combinedClickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {},
                                        onDoubleClick = component::onMaximize,
                                        onLongClick = null,
                                        enabled = !uiState.config.useSystemWindowFrame
                                    )
                                ) {
                                    Surface(
                                        modifier = Modifier.height(40.dp).fillMaxWidth()
                                            .background(color = LocalPalette.current.surface0)
                                    ) {
                                        ToolbarContent(component.toolbarComponent)
                                    }
                                }
                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = LocalPalette.current.surface0
                                    ) {}
                                    Surface(
                                        modifier = Modifier.align(Alignment.TopStart)
                                            .size(maxWidth - 10.dp, maxHeight - 10.dp),
                                        color = LocalPalette.current.base
                                    ) {
                                        NavigationContent(component.navigationComponent)
                                    }
                                    AccountListContent(component.accountListComponent)
                                    NotificationListContent(component.notificationListComponent)
                                }
                            }
                        }
                    }
                    DialogContent(component.dialogComponent)
                }
            }
        }
    }
}

@Composable
private fun MainAppWindow(
    config: ConfigurationData,
    windowState: WindowState,
    onClose: () -> Unit,
    content: @Composable (WindowScope.() -> Unit)
) {
    if (config.useSystemWindowFrame) {
        AppWindow(
            onClose = onClose,
            windowState = windowState,
            undecorated = false,
            content = content
        )
    } else {
        AppWindow(
            onClose = onClose,
            windowState = windowState,
            undecorated = true,
            content = content
        )
    }
}
package com.skyecodes.vercors.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.skyecodes.vercors.APP_NAME
import com.skyecodes.vercors.component.RootComponent
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.ui.accounts.AccountsContent
import com.skyecodes.vercors.ui.dialog.CreateNewInstanceDialogContent
import com.skyecodes.vercors.ui.dialog.ErrorDialogContent
import com.skyecodes.vercors.ui.home.HomeContent
import com.skyecodes.vercors.ui.instances.InstancesContent
import com.skyecodes.vercors.ui.search.SearchContent
import com.skyecodes.vercors.ui.settings.SettingsContent
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

    val configuration by component.configuration.collectAsState()
    val instances by component.instances.collectAsState()
    val uiState by component.uiState.collectAsState()

    uiState.fatalError?.let {
        CompositionLocalProvider(
            LocalPalette provides UI.Palette.Mocha,
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
                            Text(it.localizedMessage)
                        }
                    }
                }
            }
        }
    } ?: configuration?.let { safeConfiguration ->
        instances?.let {
            if (safeConfiguration.useSystemWindowFrame) {
                Window(
                    state = windowState,
                    onCloseRequest = ::onClose,
                    undecorated = false,
                    title = APP_NAME,
                ) {
                    AppContent(component, safeConfiguration, uiState, ::onClose)
                }
            } else {
                Window(
                    state = windowState,
                    onCloseRequest = ::onClose,
                    undecorated = true,
                    title = APP_NAME,
                ) {
                    AppContent(component, safeConfiguration, uiState, ::onClose)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
private fun FrameWindowScope.AppContent(
    component: RootComponent,
    configuration: Configuration,
    uiState: RootComponent.UiState,
    onClose: () -> Unit
) {
    LaunchedEffect(component) {
        component.initializeWindow(window)
    }

    val children by component.children.subscribeAsState()
    val currentTab by component.activeTab.collectAsState()
    val dialog by component.dialog.subscribeAsState()

    CompositionLocalProvider(
        LocalPalette provides uiState.palette,
        LocalConfiguration provides configuration
    ) {
        MaterialTheme(
            colors = uiState.palette.material(configuration.accentColor.ofPalette(LocalPalette.current)),
            typography = UI.typography,
            //shapes = UI.shapes
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row {
                    Surface(
                        modifier = Modifier.fillMaxHeight().background(color = uiState.palette.surface0)
                            .shadow(4.dp)
                    ) {
                        Menu(currentTab, component::navigate, component::openNewInstanceDialog)
                    }
                    Column {
                        WindowDraggableArea(
                            modifier = Modifier.combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {},
                                onDoubleClick = component::onMaximize,
                                onLongClick = null,
                                enabled = !configuration.useSystemWindowFrame
                            )
                        ) {
                            Surface(
                                modifier = Modifier.height(40.dp).fillMaxWidth()
                                    .background(color = uiState.palette.surface0)
                            ) {
                                Toolbar(
                                    currentTab.localizedTitle(LocalLocalization.current),
                                    children.hasPreviousScreen,
                                    children.hasNextScreen,
                                    children.canRefreshScreen,
                                    component::onNextScreen,
                                    component::onPreviousScreen,
                                    component::onRefreshScreen,
                                    component::onMinimize,
                                    component::onMaximize,
                                    onClose
                                )
                            }
                        }
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = uiState.palette.surface0
                            ) {}
                            Surface(
                                modifier = Modifier.align(Alignment.TopStart).size(maxWidth - 10.dp, maxHeight - 10.dp),
                                color = uiState.palette.base
                            ) {
                                Crossfade(
                                    targetState = children,
                                    animationSpec = if (configuration.animations) tween() else tween(0)
                                ) {
                                    when (val child = it.active.instance) {
                                        is RootComponent.ScreenChild.Home -> HomeContent(child.component)
                                        is RootComponent.ScreenChild.Instances -> InstancesContent(child.component)
                                        is RootComponent.ScreenChild.Search -> SearchContent(child.component)
                                        is RootComponent.ScreenChild.Accounts -> AccountsContent(child.component)
                                        is RootComponent.ScreenChild.Settings -> SettingsContent(child.component)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Crossfade(
                targetState = dialog,
                animationSpec = if (configuration.animations) tween() else tween(0)
            ) {
                when (val child = it.active.instance) {
                    is RootComponent.DialogChild.CreateNewInstance -> AppDialogContent(
                        modifier = Modifier.width(570.dp),
                        onClose = component::closeDialog
                    ) {
                        CreateNewInstanceDialogContent(child.component)
                    }

                    is RootComponent.DialogChild.Error -> AppDialogContent(
                        modifier = Modifier.width(500.dp),
                        onClose = component::closeDialog
                    ) {
                        ErrorDialogContent(child.component)
                    }
                    RootComponent.DialogChild.None -> {}
                }
            }
        }
    }
}

@Composable
private fun AppDialogContent(modifier: Modifier, onClose: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(LocalPalette.current.transparentOverlay)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClose
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
        ) {
            content()
        }
    }
}

private fun ApplicationScope.onClose() {
    exitApplication()
    logger.info { "Goodbye!" }
}
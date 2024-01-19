package com.skyecodes.vercors.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.skyecodes.vercors.APP_NAME
import com.skyecodes.vercors.data.model.app.AppScene
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.logic.AppViewModel
import com.skyecodes.vercors.ui.accounts.AccountsContent
import com.skyecodes.vercors.ui.home.HomeContent
import com.skyecodes.vercors.ui.instances.CreateInstanceDialogContent
import com.skyecodes.vercors.ui.instances.InstancesContent
import com.skyecodes.vercors.ui.search.SearchContent
import com.skyecodes.vercors.ui.settings.SettingsContent
import io.github.oshai.kotlinlogging.KotlinLogging
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.stateholder.LocalSavedStateHolder
import org.koin.core.parameter.parametersOf

private val logger = KotlinLogging.logger {}

@Composable
fun ApplicationScope.AppWindow(
    viewModel: AppViewModel,
) {
    val windowState = rememberWindowState(size = DpSize(1280.dp, 720.dp))
    LaunchedEffect(viewModel) {
        viewModel.initialize(windowState)
    }

    val configuration by viewModel.configuration.collectAsState()

    configuration?.let { safeConfiguration ->
        if (safeConfiguration.useSystemWindowFrame) {
            Window(
                state = viewModel.windowState,
                onCloseRequest = ::onClose,
                undecorated = false,
                title = APP_NAME,
            ) {
                AppContent(viewModel, safeConfiguration, ::onClose)
            }
        } else {
            Window(
                state = viewModel.windowState,
                onCloseRequest = ::onClose,
                undecorated = true,
                title = APP_NAME,
            ) {
                AppContent(viewModel, safeConfiguration, ::onClose)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FrameWindowScope.AppContent(
    viewModel: AppViewModel,
    configuration: Configuration,
    onClose: () -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.initializeWindow(window)
    }

    val uiState by viewModel.uiState.collectAsState()
    val currentScene by viewModel.currentScene.collectAsState()
    val savedStateHolder = LocalSavedStateHolder.current

    CompositionLocalProvider(
        LocalPalette provides uiState.palette,
        LocalConfiguration provides configuration
    ) {
        MaterialTheme(
            colors = LocalPalette.current.material,
            typography = UI.typography,
            //shapes = UI.shapes
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row {
                    Surface(
                        modifier = Modifier.fillMaxHeight().background(color = LocalPalette.current.surface0)
                            .shadow(4.dp)
                    ) {
                        Menu(currentScene) { viewModel.navigate(it) }
                    }
                    Column {
                        WindowDraggableArea(
                            modifier = Modifier.combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {},
                                onDoubleClick = viewModel::onMaximize,
                                onLongClick = null,
                                enabled = !configuration.useSystemWindowFrame
                            )
                        ) {
                            Surface(
                                modifier = Modifier.height(40.dp).fillMaxWidth()
                                    .background(color = LocalPalette.current.surface0)
                            ) {
                                Toolbar(
                                    currentScene,
                                    viewModel::onNextScene,
                                    viewModel::onPreviousScene,
                                    viewModel::onRefresh,
                                    viewModel::onMinimize,
                                    viewModel::onMaximize,
                                    onClose
                                )
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
                                modifier = Modifier.align(Alignment.TopStart).size(maxWidth - 10.dp, maxHeight - 10.dp),
                                color = LocalPalette.current.base
                            ) {
                                NavHost(
                                    navigator = viewModel.navigator,
                                    initialRoute = LocalConfiguration.current.defaultScene.route,
                                    navTransition = if (LocalConfiguration.current.animations) remember {
                                        NavTransition(
                                            createTransition = UI.transitionIn,
                                            destroyTransition = UI.transitionOut,
                                            pauseTransition = UI.transitionOut,
                                            resumeTransition = UI.transitionIn
                                        )
                                    } else remember {
                                        NavTransition(
                                            createTransition = EnterTransition.None,
                                            destroyTransition = ExitTransition.None,
                                            pauseTransition = ExitTransition.None,
                                            resumeTransition = EnterTransition.None
                                        )
                                    }
                                ) {
                                    scene(AppScene.Home.route) {
                                        HomeContent(koinViewModel {
                                            parametersOf(
                                                viewModel.configuration,
                                                viewModel.instances,
                                                savedStateHolder
                                            )
                                        })
                                    }
                                    scene(AppScene.Instances.route) {
                                        InstancesContent(koinViewModel {
                                            parametersOf(
                                                viewModel.instances,
                                                viewModel::openNewInstanceDialog,
                                                viewModel::closeNewInstanceDialog
                                            )
                                        })
                                    }
                                    scene(AppScene.Search.route) {
                                        SearchContent()
                                    }
                                    scene(AppScene.Accounts.route) {
                                        AccountsContent()
                                    }
                                    scene(AppScene.Settings.route) {
                                        SettingsContent(koinViewModel {
                                            parametersOf({ c: Configuration ->
                                                viewModel.updateConfiguration(c)
                                            })
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
                AppDialog(uiState.showNewInstanceDialog, viewModel::closeNewInstanceDialog) {
                    CreateInstanceDialogContent(viewModel::closeNewInstanceDialog)
                }
            }
        }
    }
}

@Composable
private fun AppDialog(visible: Boolean, onClose: () -> Unit, content: @Composable () -> Unit) {
    if (LocalConfiguration.current.animations) {
        AnimatedVisibility(visible, enter = UI.transitionIn, exit = UI.transitionOut) {
            AppDialogContent(onClose, content)
        }
    } else if (visible) {
        AppDialogContent(onClose, content)
    }
}

@Composable
private fun AppDialogContent(onClose: () -> Unit, content: @Composable () -> Unit) {
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
            modifier = Modifier.fillMaxWidth(0.5f)
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
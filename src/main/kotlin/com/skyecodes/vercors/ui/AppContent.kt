package com.skyecodes.vercors.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.skyecodes.vercors.component.RootComponent
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.ui.dialog.AddAccountDialogContent
import com.skyecodes.vercors.ui.dialog.AppDialogContent
import com.skyecodes.vercors.ui.dialog.CreateNewInstanceDialogContent
import com.skyecodes.vercors.ui.dialog.ErrorDialogContent
import com.skyecodes.vercors.ui.home.HomeContent
import com.skyecodes.vercors.ui.instances.InstancesContent
import com.skyecodes.vercors.ui.search.SearchContent
import com.skyecodes.vercors.ui.settings.SettingsContent
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FrameWindowScope.AppContent(
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
    //val accounts by component.accounts.subscribeAsState()

    CompositionLocalProvider(
        LocalPalette provides uiState.palette,
        LocalConfiguration provides configuration
    ) {
        MaterialTheme(
            colors = uiState.palette.material(configuration.accentColor.ofPalette(LocalPalette.current)),
            typography = UI.typography
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row {
                    Surface(
                        modifier = Modifier.fillMaxHeight().background(color = uiState.palette.surface0)
                            .shadow(4.dp)
                    ) {
                        Menu(currentTab, component::navigate, component::openNewInstanceDialog, component::openAccounts)
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
                                    currentTab?.localizedTitle?.let { it(LocalLocalization.current) },
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
                                    animationSpec = if (configuration.animations && !uiState.isFirstNavigation) tween() else tween(
                                        0
                                    )
                                ) {
                                    when (val child = it.active.instance) {
                                        is RootComponent.ScreenChild.None -> {}
                                        is RootComponent.ScreenChild.Home -> HomeContent(child.component)
                                        is RootComponent.ScreenChild.Instances -> InstancesContent(child.component)
                                        is RootComponent.ScreenChild.Search -> SearchContent(child.component)
                                        is RootComponent.ScreenChild.Settings -> SettingsContent(child.component)
                                    }
                                }
                            }
                            if (uiState.accountsPopupOpen) {
                                // TODO accounts popup
                            }
                        }
                    }
                }
            }
            Crossfade(
                targetState = dialog,
                animationSpec = if (configuration.animations) tween() else tween(0)
            ) {
                when (val child = it.child?.instance) {
                    is RootComponent.DialogChild.CreateNewInstance -> AppDialogContent(
                        modifier = Modifier.width(570.dp),
                        onClose = component::closeDialog
                    ) {
                        CreateNewInstanceDialogContent(child.component)
                    }

                    is RootComponent.DialogChild.AddAccount -> AppDialogContent(
                        modifier = Modifier.width(500.dp),
                        onClose = component::closeDialog
                    ) {
                        AddAccountDialogContent(child.component)
                    }

                    is RootComponent.DialogChild.Error -> AppDialogContent(
                        modifier = Modifier.width(500.dp),
                        onClose = component::closeDialog
                    ) {
                        ErrorDialogContent(child.component)
                    }

                    else -> {}
                }
            }
        }
    }
}

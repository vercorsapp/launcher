package com.skyecodes.vercors.root

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
import com.skyecodes.vercors.LocalConfiguration
import com.skyecodes.vercors.UI
import com.skyecodes.vercors.accounts.AccountState
import com.skyecodes.vercors.accounts.AccountsPopup
import com.skyecodes.vercors.common.LocalLocalization
import com.skyecodes.vercors.common.LocalPalette
import com.skyecodes.vercors.dialog.AppDialogContent
import com.skyecodes.vercors.dialog.error.ErrorDialogContent
import com.skyecodes.vercors.dialog.login.LoginDialogContent
import com.skyecodes.vercors.dialog.newinstance.CreateNewInstanceDialogContent
import com.skyecodes.vercors.home.HomeContent
import com.skyecodes.vercors.instances.InstancesContent
import com.skyecodes.vercors.instances.instance.InstanceDetailsContent
import com.skyecodes.vercors.menu.Menu
import com.skyecodes.vercors.projects.SearchContent
import com.skyecodes.vercors.settings.Configuration
import com.skyecodes.vercors.settings.SettingsContent
import com.skyecodes.vercors.toolbar.Toolbar

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
    val accountState by component.accountState.collectAsState()
    val selectedAccount by component.selectedAccount.collectAsState(null)

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
                        Menu(
                            currentTab = currentTab,
                            onNavigate = component::navigate,
                            selectedAccount = selectedAccount,
                            onCreateNewInstance = component::openNewInstanceDialog,
                            onOpenAccounts = component::toggleAccountsPopupOrOpenDialog
                        )
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
                                    screenTitle = currentTab?.localizedTitle?.let { it(LocalLocalization.current) }, // TODO add links to title bar
                                    hasPreviousScreen = children.hasPreviousScreen,
                                    hasNextScreen = children.hasNextScreen,
                                    canRefreshScreen = children.canRefreshScreen,
                                    onNextScreen = component::onNextScreen,
                                    onPreviousScreen = component::onPreviousScreen,
                                    onRefreshScreen = component::onRefreshScreen,
                                    onMinimize = component::onMinimize,
                                    onMaximize = component::onMaximize,
                                    onClose = onClose
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
                                        is RootComponent.ScreenChild.InstanceDetails -> InstanceDetailsContent(child.component)
                                    }
                                }
                            }
                            if (uiState.accountsPopupOpen) {
                                AccountsPopup(
                                    accountState = accountState as AccountState.Loaded,
                                    onTogglePopup = component::toggleAccountsPopup,
                                    onSelectAccount = component::selectAccount,
                                    onRemoveAccount = component::removeAccount,
                                    onAddAccount = {
                                        component.toggleAccountsPopup()
                                        component.openAddAccountDialog()
                                    }
                                )
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
                        onClose = child.component::close
                    ) {
                        CreateNewInstanceDialogContent(child.component)
                    }

                    is RootComponent.DialogChild.Login -> AppDialogContent(
                        modifier = Modifier.width(500.dp),
                        onClose = child.component::close
                    ) {
                        LoginDialogContent(child.component)
                    }

                    is RootComponent.DialogChild.Error -> AppDialogContent(
                        modifier = Modifier.width(500.dp),
                        onClose = child.component.onClose
                    ) {
                        ErrorDialogContent(child.component)
                    }

                    else -> {}
                }
            }
        }
    }
}

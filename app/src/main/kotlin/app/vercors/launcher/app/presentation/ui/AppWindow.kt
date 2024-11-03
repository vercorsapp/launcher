package app.vercors.launcher.app.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.vercors.launcher.app.presentation.action.MenuBarAction
import app.vercors.launcher.app.presentation.state.AppDialog
import app.vercors.launcher.app.presentation.util.currentTab
import app.vercors.launcher.app.presentation.util.defaultDestination
import app.vercors.launcher.app.presentation.util.screenName
import app.vercors.launcher.app.presentation.util.screenType
import app.vercors.launcher.app.presentation.viewmodel.AppViewModel
import app.vercors.launcher.core.generated.resources.app_title
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.theme.VercorsTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import java.awt.Dimension

@Composable
fun ApplicationScope.AppWindow(windowState: WindowState = rememberWindowState()) {
    val viewModel = koinInject<AppViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    WindowDecorationHandler(
        state = windowState,
        undecorated = uiState.undecorated
    ) {
        window.minimumSize = Dimension(800, 600)
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val screenType by remember { derivedStateOf { currentBackStackEntry?.destination?.screenType } }
        val screenName by remember { derivedStateOf { screenType.screenName ?: CoreString.app_title } }
        val currentTab by remember { derivedStateOf { screenType.currentTab } }

        VercorsTheme(darkTheme = true) {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                Column(modifier = Modifier.fillMaxSize()) {
                    MenuBar(
                        screenName = stringResource(screenName),
                        hasWindowControls = uiState.undecorated,
                        isMaximized = windowState.placement == WindowPlacement.Maximized,
                        onAction = {
                            when (it) {
                                MenuBarAction.Close -> exitApplication()
                                MenuBarAction.Maximize -> windowState.placement =
                                    if (windowState.placement == WindowPlacement.Maximized) WindowPlacement.Floating else WindowPlacement.Maximized

                                MenuBarAction.Minimize -> windowState.isMinimized = true
                            }
                        },
                    )
                    Row(modifier = Modifier.weight(1f)) {
                        NavigationBar(
                            currentTab = currentTab,
                            onTabSelection = { navController.navigate(it.defaultDestination) }
                        )
                        Box(modifier = Modifier.padding(bottom = 5.dp, end = 5.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AppNavHost(
                                    navController = navController,
                                    onAction = viewModel::onAction
                                )
                            }
                        }
                    }
                }
            }

            when (uiState.currentDialog) {
                AppDialog.AddAccount -> TODO()
                AppDialog.CreateInstance -> TODO()
                null -> {}
            }
        }
    }
}


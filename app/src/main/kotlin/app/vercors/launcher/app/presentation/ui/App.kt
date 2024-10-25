package app.vercors.launcher.app.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.vercors.launcher.app.presentation.action.MenuBarAction
import app.vercors.launcher.app.presentation.theme.VercorsTheme
import app.vercors.launcher.app.presentation.util.currentTab
import app.vercors.launcher.app.presentation.util.defaultDestination
import app.vercors.launcher.app.presentation.util.screenName
import app.vercors.launcher.app.presentation.viewmodel.AppViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.awt.Dimension

@Composable
fun ApplicationScope.App() {
    val windowState = rememberWindowState()

    Window(
        state = windowState,
        undecorated = true,
        onCloseRequest = ::exitApplication
    ) {
        window.minimumSize = Dimension(800, 600)
        val viewModel = koinViewModel<AppViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        VercorsTheme(darkTheme = true) {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                Column(modifier = Modifier.fillMaxSize()) {
                    MenuBar(
                        screenName = currentBackStackEntry?.destination.screenName,
                        searchQuery = uiState.searchQuery,
                        isMaximized = windowState.placement == WindowPlacement.Maximized,
                        onAction = {
                            when (it) {
                                MenuBarAction.Close -> exitApplication()
                                MenuBarAction.Maximize -> windowState.placement =
                                    if (windowState.placement == WindowPlacement.Maximized) WindowPlacement.Floating else WindowPlacement.Maximized

                                MenuBarAction.Minimize -> windowState.isMinimized = true
                                else -> viewModel.onAction(it)
                            }
                        },
                    )
                    Row(modifier = Modifier.weight(1f)) {
                        NavigationBar(
                            currentTab = currentBackStackEntry?.destination.currentTab,
                            onTabSelection = { navController.navigate(it.defaultDestination) }
                        )
                        Box(modifier = Modifier.padding(bottom = 5.dp, end = 5.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                MainContent(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}


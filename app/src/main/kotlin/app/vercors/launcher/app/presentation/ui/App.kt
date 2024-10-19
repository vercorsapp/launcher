package app.vercors.launcher.app.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import app.vercors.launcher.app.presentation.action.AppAction
import app.vercors.launcher.app.presentation.theme.VercorsTheme
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

        VercorsTheme(darkTheme = true) {
            Surface(color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.fillMaxSize()) {
                    MenuBar(
                        searchQuery = uiState.searchQuery,
                        isMaximized = windowState.placement == WindowPlacement.Maximized,
                        onMinimize = { windowState.isMinimized = true },
                        onMaximize = { windowState.placement = if (windowState.placement == WindowPlacement.Maximized) WindowPlacement.Floating else WindowPlacement.Maximized },
                        onClose = ::exitApplication,
                        onSearchQueryChange = { viewModel.onAction(AppAction.SearchQueryChange(it)) }
                    )
                    Row(modifier = Modifier.weight(1f)) {
                        NavigationBar()
                    }
                }
            }
        }
    }
}


package app.vercors.launcher.app.setup

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.vercors.launcher.app.presentation.theme.VercorsTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ApplicationScope.SetupWindow() {
    Window(onCloseRequest = ::exitApplication) {
        VercorsTheme(darkTheme = true) {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                val viewModel = koinViewModel<SetupViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                SetupContent(
                    uiState = uiState,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}


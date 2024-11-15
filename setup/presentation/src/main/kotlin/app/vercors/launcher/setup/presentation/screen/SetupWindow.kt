package app.vercors.launcher.setup.presentation.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.domain.APP_NAME
import app.vercors.launcher.core.presentation.theme.VercorsTheme
import app.vercors.launcher.core.presentation.viewmodel.MviContainer
import app.vercors.launcher.setup.presentation.viewmodel.SetupUiEffect
import app.vercors.launcher.setup.presentation.viewmodel.SetupViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ApplicationScope.SetupWindow(
    onLaunch: () -> Unit,
) {
    Window(
        title = APP_NAME,
        onCloseRequest = ::exitApplication
    ) {
        VercorsTheme(
            theme = GeneralConfig.DEFAULT.theme,
            accent = GeneralConfig.DEFAULT.accent,
        ) {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                MviContainer(
                    viewModel = koinViewModel<SetupViewModel>(),
                    onEffect = {
                        when (it) {
                            SetupUiEffect.Launch -> onLaunch()
                        }
                    }
                ) { state, onIntent ->
                    SetupContent(
                        uiState = state,
                        onIntent = onIntent
                    )
                }
            }
        }
    }
}


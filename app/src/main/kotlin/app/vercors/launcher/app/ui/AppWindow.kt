package app.vercors.launcher.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import app.vercors.launcher.app.state.AppDialog
import app.vercors.launcher.app.state.AppUiState
import app.vercors.launcher.app.state.GeneralConfigState
import app.vercors.launcher.core.generated.resources.app_title
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.theme.VercorsTheme
import app.vercors.launcher.core.presentation.ui.AppAnimations
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension

private val logger = KotlinLogging.logger {}

@Composable
fun ApplicationScope.AppWindow(
    uiState: AppUiState,
    windowState: WindowState = rememberWindowState()
) {
    if (uiState.generalConfig is GeneralConfigState.Loaded) {
        Window(
            state = windowState,
            title = stringResource(CoreString.app_title),
            undecorated = !uiState.generalConfig.decorated,
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(800, 600)
            val navController = rememberNavController()

            VercorsTheme(
                theme = uiState.generalConfig.theme,
                accent = uiState.generalConfig.accent
            ) {
                CompositionLocalProvider(
                    AppAnimations provides uiState.generalConfig.animations,
                ) {
                    Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                        AppContent(
                            navController = navController,
                            windowState = windowState,
                            generalConfig = uiState.generalConfig,
                            onClose = ::exitApplication
                        )
                    }

                    if (uiState.currentDialog != null) {
                        when (uiState.currentDialog) {
                            AppDialog.AddAccount -> TODO()
                            AppDialog.CreateInstance -> TODO()
                        }
                    }
                }
            }
        }
    }
}

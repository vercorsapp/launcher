package app.vercors.launcher.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import app.vercors.launcher.app.viewmodel.AppViewModel
import app.vercors.launcher.app.viewmodel.GeneralConfigState
import app.vercors.launcher.core.generated.resources.app_title
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.theme.VercorsTheme
import app.vercors.launcher.core.presentation.ui.AppAnimations
import app.vercors.launcher.core.presentation.ui.AppCrossfade
import app.vercors.launcher.core.presentation.viewmodel.ViewModelContainer
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import java.awt.Dimension

@Composable
fun ApplicationScope.AppWindow(
    windowState: WindowState = rememberWindowState()
) {
    ViewModelContainer(
        viewModel = koinInject<AppViewModel>(),
        minActiveState = null
    ) { state, onIntent ->
        if (state.generalConfig is GeneralConfigState.Loaded) {
            Window(
                state = windowState,
                title = stringResource(CoreString.app_title),
                undecorated = !state.generalConfig.decorated,
                onCloseRequest = ::exitApplication,
            ) {
                window.minimumSize = Dimension(800, 600)
                val navController = rememberNavController()

                VercorsTheme(
                    theme = state.generalConfig.theme,
                    accent = state.generalConfig.accent
                ) {
                    CompositionLocalProvider(
                        AppAnimations provides state.generalConfig.animations
                    ) {
                        Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                            AppContent(
                                navController = navController,
                                windowState = windowState,
                                generalConfig = state.generalConfig,
                                onClose = ::exitApplication,
                                onIntent = onIntent
                            )
                        }

                        AppCrossfade(
                            targetState = state.currentDialog,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            it?.let { dialog ->
                                AppDialogContent(
                                    dialog = dialog,
                                    onIntent = onIntent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

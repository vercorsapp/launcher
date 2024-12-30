/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.launcher.app.ui

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import app.vercors.launcher.app.viewmodel.AppViewModel
import app.vercors.launcher.app.viewmodel.GeneralConfigState
import app.vercors.launcher.core.presentation.mvi.MviContainer
import app.vercors.launcher.core.presentation.theme.VercorsTheme
import app.vercors.launcher.core.presentation.ui.*
import app.vercors.launcher.core.resources.appStringResource
import app.vercors.launcher.core.resources.app_title
import org.koin.compose.koinInject
import java.awt.Dimension

@Composable
fun ApplicationScope.AppWindow(
    windowState: WindowState = rememberWindowState()
) {
    MviContainer(
        viewModel = koinInject<AppViewModel>(),
        minActiveState = null
    ) { state, onIntent ->
        if (state.generalConfig is GeneralConfigState.Loaded) {
            Window(
                state = windowState,
                title = appStringResource { app_title },
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
                            if (it != null) Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))) {}
                        }

                        AppAnimatedContent(
                            targetState = state.currentDialog,
                            transitionSpec = { defaultEnterAnimation + scaleIn() togetherWith defaultExitAnimation + scaleOut() },
                        ) {
                            AppDialogContent(
                                dialog = it,
                                onIntent = onIntent
                            )
                        }
                    }
                }
            }
        }
    }
}

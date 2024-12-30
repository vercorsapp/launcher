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

package app.vercors.launcher.setup.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.domain.APP_NAME
import app.vercors.launcher.core.presentation.mvi.MviContainer
import app.vercors.launcher.core.presentation.theme.VercorsTheme
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


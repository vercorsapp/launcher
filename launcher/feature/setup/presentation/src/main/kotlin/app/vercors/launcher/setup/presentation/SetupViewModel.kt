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

import app.vercors.launcher.core.domain.APP_NAME
import app.vercors.launcher.core.presentation.mvi.MviViewModel
import app.vercors.launcher.setup.domain.SetupRepository
import kotlinx.io.files.Path
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SetupViewModel(
    private val setupRepository: SetupRepository
) : MviViewModel<SetupUiState, SetupUiEvent, SetupUiEffect>(SetupUiState(setupRepository.defaultPath)) {
    override fun SetupUiState.reduce(intent: SetupUiEvent): SetupUiState =
        when (intent) {
            is SetupUiEvent.PickDirectory -> pickDirectory(intent.path)
            is SetupUiEvent.UpdatePath -> updatePath(intent.path)
            SetupUiEvent.StartApp -> launchApp()
        }

    private fun SetupUiState.pickDirectory(path: String): SetupUiState =
        updatePath(if (path.endsWith(APP_NAME)) path else Path(path, APP_NAME).toString())

    private fun SetupUiState.updatePath(path: String): SetupUiState = SetupUiState(path)

    private fun SetupUiState.launchApp(): SetupUiState {
        val path = state.value.path
        setupRepository.updatePath(path)
        logger.info { "User selected directory: $path - now launching application" }
        return withEffect(SetupUiEffect.Launch)
    }
}
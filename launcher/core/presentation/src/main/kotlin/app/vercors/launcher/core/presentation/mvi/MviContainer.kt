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

package app.vercors.launcher.core.presentation.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.vercors.launcher.core.presentation.ui.ObserveAsEvents

@Composable
fun <State : Any, Intent : Any, Effect : Any> MviContainer(
    viewModel: MviViewModel<State, Intent, Effect>,
    onEffect: (Effect) -> Unit,
    minActiveState: Lifecycle.State? = Lifecycle.State.STARTED,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    ObserveAsEvents(viewModel.effects, onEvent = onEffect)
    MviContainer(
        viewModel = viewModel,
        minActiveState = minActiveState,
        content = content
    )
}

@Composable
fun <State : Any, Intent : Any, Effect : Any> MviContainer(
    viewModel: MviViewModel<State, Intent, Effect>,
    minActiveState: Lifecycle.State? = Lifecycle.State.STARTED,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    val uiState by viewModel.state.run {
        if (minActiveState != null) collectAsStateWithLifecycle(minActiveState = minActiveState)
        else collectAsState()
    }
    content(uiState, viewModel::onIntent)
}
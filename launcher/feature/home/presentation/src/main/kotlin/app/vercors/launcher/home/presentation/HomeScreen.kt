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

package app.vercors.launcher.home.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.launcher.core.presentation.mvi.MviContainer
import app.vercors.launcher.core.presentation.ui.AppScrollableLazyColumn
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onEffect: (HomeUiEffect) -> Unit,
) {
    MviContainer(
        viewModel = koinViewModel<HomeViewModel>(),
        onEffect = onEffect
    ) { state, onIntent ->
        HomeScreen(
            state = state,
            onIntent = onIntent
        )
    }
}

@Composable
internal fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeUiIntent) -> Unit
) {
    AppScrollableLazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.sections) { section ->
            HomeSection(
                section = section,
                onIntent = onIntent
            )
        }
    }
}
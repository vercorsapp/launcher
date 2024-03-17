/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.WindowState
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent

@Composable
fun RootContent(component: RootComponent, windowState: WindowState, onClose: () -> Unit) {
    val childState by component.childState.collectAsState()

    when (val childComponent = childState.child?.instance) {
        is ErrorComponent -> AppWindow(
                onClose = onClose,
                windowState = windowState
        ) {
            LaunchedEffect(Unit) {
                window.setSize(600, 400)
            }
            ErrorContent(childComponent)
        }

        is MainComponent -> MainContent(childComponent, windowState, onClose)
        is SetupComponent -> AppWindow(
                onClose = onClose,
                windowState = windowState
        ) {
            LaunchedEffect(Unit) {
                window.setSize(800, 320)
            }
            SetupContent(childComponent)
        }
    }
}

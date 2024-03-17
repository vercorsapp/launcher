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

package app.vercors.dialog

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.vercors.LocalConfiguration
import app.vercors.LocalPalette
import app.vercors.dialog.error.ErrorDialogComponent
import app.vercors.dialog.error.ErrorDialogContent
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.instance.CreateInstanceDialogContent
import app.vercors.dialog.login.LoginDialogComponent
import app.vercors.dialog.login.LoginDialogContent

@Composable
fun DialogContent(component: DialogComponent) {
    val childState by component.childState.collectAsState()

    Crossfade(
        targetState = childState,
        animationSpec = if (LocalConfiguration.current.animations) tween() else tween(0)
    ) {
        it.child?.instance?.let { childComponent ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(LocalPalette.current.transparentOverlay)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = component::closeDialog
                    ),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                ) {
                    when (childComponent) {
                        is CreateInstanceDialogComponent -> CreateInstanceDialogContent(childComponent)
                        is LoginDialogComponent -> LoginDialogContent(childComponent)
                        is ErrorDialogComponent -> ErrorDialogContent(childComponent)
                    }
                }
            }
        }
    }
}

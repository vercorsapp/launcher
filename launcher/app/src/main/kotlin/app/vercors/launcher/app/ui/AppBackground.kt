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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.ui.runComposable

@Composable
fun ContentBackground(
    gradient: Boolean,
    content: @Composable () -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val modifier: @Composable Modifier.() -> Modifier = if (gradient) {
        {
            background(
                largeRadialGradient(
                    background,
                    lerp(background, MaterialTheme.colorScheme.primaryContainer, 0.5f)
                )
            )
        }
    } else {
        { background(background) }
    }

    Box(modifier = Modifier.padding(bottom = 5.dp, end = 5.dp).fillMaxSize().runComposable(modifier)) {
        content()
    }
}

@Stable
private fun largeRadialGradient(background0: Color, background1: Color) = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        return RadialGradientShader(
            colors = listOf(background0, background0, background1),
            colorStops = listOf(0f, 0.3f, 1f),
            center = Offset(0f, 0f),
            radius = (size.height + size.width) / 1.5f,
        )
    }
}
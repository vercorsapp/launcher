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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import app.vercors.AppColors
import app.vercors.AppTypography
import app.vercors.LocalConfiguration
import app.vercors.LocalPalette
import app.vercors.common.AppColoredPalette
import app.vercors.common.AppPalette
import app.vercors.configuration.ConfigurationData

@Composable
fun AppWindowContent(
    config: ConfigurationData = ConfigurationData.DEFAULT,
    palette: AppPalette = AppPalette.Macchiato,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalConfiguration provides config,
        LocalPalette provides AppColoredPalette(palette)
    ) {
        MaterialTheme(
            colors = AppColors,
            typography = AppTypography
        ) {
            Surface(
                color = LocalPalette.current.base,
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}
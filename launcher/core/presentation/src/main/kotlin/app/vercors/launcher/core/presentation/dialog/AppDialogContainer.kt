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

package app.vercors.launcher.core.presentation.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.card.AppCard
import app.vercors.launcher.core.presentation.modifier.clickableWithoutRipple
import app.vercors.launcher.core.presentation.ui.AppIconTextButton
import app.vercors.launcher.core.presentation.ui.AppSectionTitle
import app.vercors.launcher.core.resources.appStringResource
import app.vercors.launcher.core.resources.appVectorResource
import app.vercors.launcher.core.resources.close
import app.vercors.launcher.core.resources.x

@Composable
fun AppDialogContainer(
    title: String,
    buttons: @Composable AppDialogButtonRowScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    AppCard(Modifier.clickableWithoutRipple()) {
        Column(
            modifier = Modifier.padding(20.dp).width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                AppSectionTitle(title)
                content()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.End),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    buttons()
                }
            }
        }
    }
}

typealias AppDialogButtonRowScope = RowScope

@Composable
fun AppDialogButtonRowScope.AppDialogCloseButton(onClick: () -> Unit) {
    AppIconTextButton(
        onClick = onClick,
        icon = appVectorResource { x },
        text = appStringResource { close },
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
    )
}
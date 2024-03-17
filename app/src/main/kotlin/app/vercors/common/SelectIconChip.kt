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

package app.vercors.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import app.vercors.UI

@Composable
fun SelectIconChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    imageVector: ImageVector
) {
    SelectIconChip(
        selected = selected,
        onClick = onClick,
        text = text
    ) {
        Icon(imageVector, null, Modifier.size(UI.mediumIconSize))
    }
}

@Composable
fun SelectIconChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    painter: Painter
) {
    SelectIconChip(
        selected = selected,
        onClick = onClick,
        text = text
    ) {
        Icon(painter, null, Modifier.size(UI.mediumIconSize))
    }
}

@Composable
fun SelectIconChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    icon: (@Composable () -> Unit)? = null
) {
    val backgroundColor by appAnimateColorAsState(selected, MaterialTheme.colors.primary, MaterialTheme.colors.surface)
    val contentColor by appAnimateColorAsState(selected, MaterialTheme.colors.onPrimary, MaterialTheme.colors.onSurface)

    FilterChip(
        selected = selected,
        onClick = onClick,
        trailingIcon = icon,
        colors = ChipDefaults.filterChipColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
    ) {
        Text(text, style = MaterialTheme.typography.button)
    }
}

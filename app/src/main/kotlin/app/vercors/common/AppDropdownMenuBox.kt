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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import app.vercors.applyIf
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp

@Composable
fun <T> AppDropdownMenuBox(
    options: Iterable<T>,
    value: T?,
    textConverter: @Composable (T) -> String,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier.width(IntrinsicSize.Max),
    leadingIcon: @Composable (() -> Unit)? = null,
    showScrollbar: Boolean = true,
    optionContent: @Composable RowScope.(T) -> Unit
) {
    val colors = MaterialTheme.colors
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.pointerHoverIcon(
            PointerIcon.Hand,
            overrideDescendants = true
        )
    ) {
        OutlinedTextField(
            value = value?.let { textConverter(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                    contentDescription = "Show options"
                )
            }
        )

        ScrollableExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            showScrollbar = showScrollbar,
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            options.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onValueChange(it)
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    modifier = Modifier.applyIf<Modifier>(it == value) { background(colors.primary) }
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides if (it == value) colors.onPrimary else colors.onSurface
                    ) {
                        optionContent(it)
                    }
                }
            }
        }
    }
}
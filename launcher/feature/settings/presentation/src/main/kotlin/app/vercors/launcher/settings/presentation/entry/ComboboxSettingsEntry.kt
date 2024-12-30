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

package app.vercors.launcher.settings.presentation.entry

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.launcher.core.presentation.ui.AppDropdownMenuBox

@Composable
@Suppress("kotlin:S107")
fun <T> ComboboxSettingsEntry(
    title: String,
    description: String,
    options: Iterable<T>,
    value: T?,
    textConverter: @Composable (T) -> String,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier.width(IntrinsicSize.Max),
    leadingIcon: @Composable (() -> Unit)? = null,
    optionContent: @Composable (T) -> Unit
) {
    SettingsEntry(
        title = title,
        description = description
    ) {
        AppDropdownMenuBox(
            options = options,
            value = value,
            textConverter = textConverter,
            leadingIcon = leadingIcon,
            onValueChange = onValueChange,
            modifier = modifier,
            optionContent = optionContent
        )
    }
}
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

package app.vercors.launcher.setup.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.ui.AppIconButton
import app.vercors.launcher.core.resources.*
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher

@Composable
fun SetupContent(
    uiState: SetupUiState,
    onIntent: (SetupUiEvent) -> Unit
) {
    val filePicker = rememberDirectoryPickerLauncher(initialDirectory = uiState.parentPath) { directory ->
        directory?.path?.let { onIntent(SetupUiEvent.PickDirectory(it)) }
    }

    Column(
        modifier = Modifier.padding(20.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = appStringResource { welcome },
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.height(20.dp))
        Text(text = appStringResource { welcome_path })
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.path,
                onValueChange = { onIntent(SetupUiEvent.UpdatePath(it)) },
                modifier = Modifier.width(450.dp)
            )
            AppIconButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onClick = filePicker::launch,
                icon = appVectorResource { folder },
                text = appStringResource { open_folder }
            )
        }
        Spacer(Modifier.height(20.dp))
        AppIconButton(
            onClick = { onIntent(SetupUiEvent.StartApp) },
            icon = appVectorResource { play },
            text = appStringResource { start_app }
        )
    }
}
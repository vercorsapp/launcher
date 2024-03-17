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

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.APP_NAME
import app.vercors.UI
import app.vercors.common.AppLabeledCheckbox
import app.vercors.common.IconTextButton
import app.vercors.root.setup.SetupComponent
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import compose.icons.FeatherIcons
import compose.icons.feathericons.Folder
import compose.icons.feathericons.Play
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@Composable
fun SetupContent(component: SetupComponent) {
    val uiState by component.uiState.collectAsState()

    AppWindowContent(
        config = uiState.config
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
            modifier = Modifier.padding(UI.largePadding)
        ) {
            Text(stringResource(Res.string.welcome, APP_NAME), style = MaterialTheme.typography.h5)
            Text(stringResource(Res.string.selectDataDir))
            Row(
                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.path,
                    onValueChange = component::updatePath,
                    modifier = Modifier.width(600.dp)
                )
                IconTextButton(
                    onClick = component::openDirectoryPicker,
                    imageVector = FeatherIcons.Folder,
                    text = stringResource(Res.string.browse),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = UI.mediumPadding)
            ) {
                AppLabeledCheckbox(
                    checked = uiState.showTutorial,
                    onCheckedChange = component::updateShowTutorial,
                    text = stringResource(Res.string.showTutorial)
                )
            }
            IconTextButton(
                onClick = component::launch,
                imageVector = FeatherIcons.Play,
                text = stringResource(Res.string.launch)
            )
        }
    }

    DirectoryPicker(
        show = uiState.showDirectoryPicker,
        initialDirectory = uiState.path
    ) { path ->
        path?.let { component.updatePath(it) }
        component.closeDirectoryPicker()
    }
}
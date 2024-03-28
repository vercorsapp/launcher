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

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.common.AppTextButton
import app.vercors.common.appAnimateContentSize
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogButtons
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponent
import app.vercors.dialog.error.launch.LaunchErrorDialogButtons
import app.vercors.dialog.error.launch.LaunchErrorDialogComponent
import app.vercors.dialog.instance.kill.KillInstanceDialogButtons
import app.vercors.dialog.instance.kill.KillInstanceDialogComponent
import compose.icons.FeatherIcons
import compose.icons.feathericons.X
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.close

@Composable
fun SimpleDialogContent(component: SimpleDialogComponent) {
    Column(
        verticalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.padding(UI.largePadding).width(500.dp).appAnimateContentSize()
    ) {
        Text(component.title, style = MaterialTheme.typography.h5)
        Text(component.message)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(UI.largePadding, Alignment.End)
        ) {
            when (component) {
                is JavaVersionErrorDialogComponent -> JavaVersionErrorDialogButtons(component)
                is LaunchErrorDialogComponent -> LaunchErrorDialogButtons(component)
                is KillInstanceDialogComponent -> KillInstanceDialogButtons(component)
            }
            AppTextButton(component::onClose) {
                Icon(
                    imageVector = FeatherIcons.X,
                    contentDescription = null,
                    modifier = Modifier.size(UI.mediumIconSize)
                )
                Text(
                    text = stringResource(Res.string.close),
                    modifier = Modifier.padding(start = UI.mediumPadding)
                )
            }
        }
    }
}
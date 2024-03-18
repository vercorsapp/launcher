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

package app.vercors.configuration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.LocalPalette
import compose.icons.FeatherIcons
import compose.icons.feathericons.CheckCircle
import compose.icons.feathericons.Info
import compose.icons.feathericons.XCircle
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.checkingJava
import vercors.ui.generated.resources.invalidJava
import vercors.ui.generated.resources.validJava

val ConfigurationJavaStatus.icon: ImageVector
    @Stable get() = when (this) {
        ConfigurationJavaStatus.Checking -> FeatherIcons.Info
        ConfigurationJavaStatus.Valid -> FeatherIcons.CheckCircle
        ConfigurationJavaStatus.Invalid -> FeatherIcons.XCircle
    }

val ConfigurationJavaStatus.color: Color
    @Composable get() = when (this) {
        ConfigurationJavaStatus.Checking -> LocalPalette.current.blue
        ConfigurationJavaStatus.Valid -> LocalPalette.current.green
        ConfigurationJavaStatus.Invalid -> LocalPalette.current.red
    }

@Composable
fun ConfigurationJavaStatus.text(javaVersion: Int): String = stringResource(
    when (this) {
        ConfigurationJavaStatus.Checking -> Res.string.checkingJava
        ConfigurationJavaStatus.Valid -> Res.string.validJava
        ConfigurationJavaStatus.Invalid -> Res.string.invalidJava
    }, javaVersion
)
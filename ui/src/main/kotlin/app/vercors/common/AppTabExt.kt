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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Home
import compose.icons.feathericons.Search
import compose.icons.feathericons.Settings
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.*

val AppTab.title: String
    @Composable get() = stringResource(
        when (this) {
            AppTab.Home -> Res.string.home
            AppTab.Instances -> Res.string.instances
            AppTab.Search -> Res.string.search
            AppTab.Settings -> Res.string.settings
        }
    )

val AppTab.icon: ImageVector
    @Stable get() = when (this) {
        AppTab.Home -> FeatherIcons.Home
        AppTab.Instances -> FeatherIcons.Box
        AppTab.Search -> FeatherIcons.Search
        AppTab.Settings -> FeatherIcons.Settings
    }
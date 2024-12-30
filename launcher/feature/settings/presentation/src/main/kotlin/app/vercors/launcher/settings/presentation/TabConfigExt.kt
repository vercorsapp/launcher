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

package app.vercors.launcher.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.resources.*

val TabConfig.displayName: String
    @Composable get() = appStringResource {
        when (this@displayName) {
            TabConfig.Home -> home
            TabConfig.Instances -> instances
            TabConfig.Projects -> projects
            TabConfig.Accounts -> accounts
            TabConfig.Settings -> settings
        }
    }

val TabConfig.icon: ImageVector
    @Composable get() = appVectorResource {
        when (this@icon) {
            TabConfig.Home -> house
            TabConfig.Instances -> hard_drive
            TabConfig.Projects -> folder_cog
            TabConfig.Accounts -> user
            TabConfig.Settings -> settings
        }
    }
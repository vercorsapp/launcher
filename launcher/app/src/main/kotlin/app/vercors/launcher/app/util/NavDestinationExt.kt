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

package app.vercors.launcher.app.util

import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import app.vercors.launcher.core.resources.*
import org.jetbrains.compose.resources.StringResource

@Deprecated("Needs to be changed to be more flexible")
typealias ScreenType = String

@Deprecated("Needs to be changed to be more flexible")
val NavDestination?.screenType: ScreenType?
    @Stable get() = this?.route?.substringAfterLast(".")?.substringBefore("?")

@Deprecated("Needs to be changed to be more flexible")
val ScreenType?.screenName: StringResource?
    @Stable get() = when (this) {
        "HomeRoute" -> appString { home }
        "InstanceListRoute" -> appString { instances }
        "ProjectListRoute" -> appString { projects }
        "AccountListRoute" -> appString { accounts }
        "SettingsRoute" -> appString { settings }
        else -> null
    }

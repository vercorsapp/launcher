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

package app.vercors.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import app.vercors.LocalConfiguration
import app.vercors.configuration.ConfigurationComponent
import app.vercors.configuration.ConfigurationContainer
import app.vercors.home.HomeComponent
import app.vercors.home.HomeContainer
import app.vercors.instance.InstanceListComponent
import app.vercors.instance.InstanceListContainer
import app.vercors.project.SearchComponent
import app.vercors.project.SearchContainer

@Composable
fun NavigationContent(state: NavigationState) {
    Crossfade(
        targetState = state.child,
        animationSpec = if (LocalConfiguration.current.animations) tween() else tween(0)
    ) {
        when (it) {
            is HomeComponent -> HomeContainer(it)
            is InstanceListComponent -> InstanceListContainer(it)
            is SearchComponent -> SearchContainer(it)
            is ConfigurationComponent -> ConfigurationContainer(it)
        }
    }
}
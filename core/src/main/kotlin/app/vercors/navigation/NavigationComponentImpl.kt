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

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.Refreshable
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationComponent
import app.vercors.home.HomeComponent
import app.vercors.instance.InstanceListComponent
import app.vercors.instance.details.InstanceDetailsComponent
import app.vercors.project.ProjectDetailsComponent
import app.vercors.project.SearchComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull

class NavigationComponentImpl(
    componentContext: AppComponentContext,
    private val navigationService: NavigationService = componentContext.inject()
) : AbstractAppComponent(componentContext), NavigationEventHandler by navigationService, NavigationComponent {
    private val navigation = StackNavigation<NavigationConfig>()
    private val _childState: Value<ChildStack<NavigationConfig, NavigationChildComponent>> = childStack(
        source = navigation,
        serializer = NavigationConfig.serializer(),
        initialConfiguration = navigationService.navigationState.value.active,
        childFactory = childFactory(::createChild)
    )
    override val childState: StateFlow<ChildStack<*, NavigationChildComponent>> = _childState.toStateFlow()

    init {
        navigationService.navigationState.filterNotNull().collectInLifecycle {
            navigation.bringToFront(it.active)
        }
        navigationService.refreshChannel.consumeInLifecycle {
            val active = childState.value.active.instance
            if (active is Refreshable) active.refresh()
        }
    }

    private fun createChild(config: NavigationConfig, componentContext: AppComponentContext): NavigationChildComponent =
        when (config) {
            NavigationConfig.Home -> inject<HomeComponent>(componentContext)
            NavigationConfig.InstanceList -> inject<InstanceListComponent>(componentContext)
            NavigationConfig.Search -> inject<SearchComponent>(componentContext)
            NavigationConfig.Configuration -> inject<ConfigurationComponent>(componentContext)
            is NavigationConfig.InstanceDetails -> inject<InstanceDetailsComponent>(componentContext)
            is NavigationConfig.ProjectDetails -> inject<ProjectDetailsComponent>(componentContext)
        }
}
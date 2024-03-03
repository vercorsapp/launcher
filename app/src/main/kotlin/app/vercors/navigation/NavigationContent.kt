package app.vercors.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.vercors.LocalConfiguration
import app.vercors.configuration.ConfigurationComponent
import app.vercors.configuration.ConfigurationContent
import app.vercors.home.HomeComponent
import app.vercors.home.HomeContent
import app.vercors.instance.InstanceListComponent
import app.vercors.instance.InstanceListContent
import app.vercors.project.SearchComponent
import app.vercors.project.SearchContent
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun NavigationContent(component: NavigationComponent) {
    val state by component.children.subscribeAsState()

    Crossfade(
        targetState = state,
        animationSpec = if (LocalConfiguration.current.animations) tween() else tween(0)
    ) {
        when (val childComponent = it.active.instance) {
            is HomeComponent -> HomeContent(childComponent)
            is InstanceListComponent -> InstanceListContent(childComponent)
            is SearchComponent -> SearchContent(childComponent)
            is ConfigurationComponent -> ConfigurationContent(childComponent)
        }
    }
}
package app.vercors.navigation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

val NavigationConfig.title: String
    @Composable get() = when (this) {
        NavigationConfig.Home -> stringResource(Res.string.home)
        NavigationConfig.InstanceList -> stringResource(Res.string.instances)
        NavigationConfig.Search -> stringResource(Res.string.search)
        NavigationConfig.Configuration -> stringResource(Res.string.settings)
        is NavigationConfig.InstanceDetails -> instance.name
        is NavigationConfig.ProjectDetails -> project.name
    }
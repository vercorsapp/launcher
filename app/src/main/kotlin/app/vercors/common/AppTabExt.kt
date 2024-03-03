package app.vercors.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Home
import compose.icons.feathericons.Search
import compose.icons.feathericons.Settings
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@OptIn(ExperimentalResourceApi::class)
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
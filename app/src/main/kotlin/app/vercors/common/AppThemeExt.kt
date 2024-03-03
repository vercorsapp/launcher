package app.vercors.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.Cpu
import compose.icons.feathericons.Moon
import compose.icons.feathericons.Sun
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.dark
import vercors.app.generated.resources.light
import vercors.app.generated.resources.system

@OptIn(ExperimentalResourceApi::class)
val AppTheme.title: String
    @Composable get() = stringResource(
        when (this) {
            AppTheme.System -> Res.string.system
            AppTheme.Light -> Res.string.light
            AppTheme.Dark -> Res.string.dark
        }
    )

val AppTheme.icon: ImageVector
    @Stable get() = when (this) {
        AppTheme.System -> FeatherIcons.Cpu
        AppTheme.Light -> FeatherIcons.Sun
        AppTheme.Dark -> FeatherIcons.Moon
    }
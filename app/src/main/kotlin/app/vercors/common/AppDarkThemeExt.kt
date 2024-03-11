package app.vercors.common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.darker
import vercors.app.generated.resources.lighter
import vercors.app.generated.resources.normal

val AppDarkTheme.title: String
    @Composable get() = stringResource(
        when (this) {
            AppDarkTheme.Lighter -> Res.string.lighter
            AppDarkTheme.Normal -> Res.string.normal
            AppDarkTheme.Darker -> Res.string.darker
        }
    )
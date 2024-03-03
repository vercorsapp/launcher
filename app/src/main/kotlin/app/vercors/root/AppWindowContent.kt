package app.vercors.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import app.vercors.AppColors
import app.vercors.AppTypography
import app.vercors.LocalConfiguration
import app.vercors.LocalPalette
import app.vercors.common.AppColoredPalette
import app.vercors.common.AppPalette
import app.vercors.configuration.ConfigurationData

@Composable
fun AppWindowContent(
    config: ConfigurationData = ConfigurationData.DEFAULT,
    palette: AppPalette = AppPalette.Macchiato,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalConfiguration provides config,
        LocalPalette provides AppColoredPalette(palette)
    ) {
        MaterialTheme(
            colors = AppColors,
            typography = AppTypography
        ) {
            Surface(
                color = LocalPalette.current.base,
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}
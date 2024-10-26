package app.vercors.launcher.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.vercors.launcher.app.AppFont
import app.vercors.launcher.app.generated.resources.InterBold
import app.vercors.launcher.app.generated.resources.InterMedium
import app.vercors.launcher.app.generated.resources.InterRegular
import org.jetbrains.compose.resources.Font

@Composable
fun VercorsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = VercorsTypography,
        content = content
    )
}

private val VercorsTypography: Typography @Composable get() {
    val fontFamily = FontFamily(
        Font(
            resource = AppFont.InterRegular,
            weight = FontWeight.Normal,
        ),
        Font(
            resource = AppFont.InterMedium,
            weight = FontWeight.Medium,
        ),
        Font(
            resource = AppFont.InterBold,
            weight = FontWeight.Bold,
        )
    )
    return Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = fontFamily),
    )
}

private val DarkColorScheme = createVercorsColorScheme(CatppuccinColors.Mocha)
private val LightColorScheme = createVercorsColorScheme(CatppuccinColors.Latte)

private fun createVercorsColorScheme(colors: CatppuccinColors) = with(colors) {
    ColorScheme(
        primary = mauve,
        onPrimary = mantle,
        primaryContainer = lerp(base, mauve, 0.2f),
        onPrimaryContainer = text,
        inversePrimary = lerp(text, mauve, 0.2f),
        secondary = mauve,
        onSecondary = mantle,
        secondaryContainer = lerp(base, mauve, 0.2f),
        onSecondaryContainer = text,
        tertiary = green,
        onTertiary = mantle,
        tertiaryContainer = lerp(base, green, 0.2f),
        onTertiaryContainer = text,
        background = base,
        onBackground = text,
        surface = surface0,
        onSurface = text,
        surfaceVariant = surface1,
        onSurfaceVariant = subtext0,
        surfaceTint = surface2,
        inverseSurface = subtext1,
        inverseOnSurface = crust,
        error = red,
        onError = mantle,
        errorContainer = lerp(base, red, 0.2f),
        onErrorContainer = text,
        outline = overlay2,
        outlineVariant = overlay1,
        scrim = Color.Black,
        surfaceBright = surface1,
        surfaceDim = surface0,
        surfaceContainer = base,
        surfaceContainerHigh = surface0,
        surfaceContainerHighest = surface1,
        surfaceContainerLow = mantle,
        surfaceContainerLowest = crust
    )
}
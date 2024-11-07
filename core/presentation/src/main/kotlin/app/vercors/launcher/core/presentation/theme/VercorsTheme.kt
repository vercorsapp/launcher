package app.vercors.launcher.core.presentation.theme

import androidx.annotation.ColorInt
import androidx.annotation.Size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.vercors.launcher.core.generated.resources.InterBold
import app.vercors.launcher.core.generated.resources.InterMedium
import app.vercors.launcher.core.generated.resources.InterRegular
import app.vercors.launcher.core.presentation.CoreFont
import org.jetbrains.compose.resources.Font

@Composable
fun VercorsTheme(
    theme: String,
    accent: String,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        "catppuccin-latte" -> CatppuccinColors.Latte
        "catppuccin-frappe" -> CatppuccinColors.Frappe
        "catppuccin-macchiato" -> CatppuccinColors.Macchiato
        "catppuccin-mocha" -> CatppuccinColors.Mocha
        else -> throw IllegalArgumentException("Unknown theme $theme")
    }
    val accentProvider: CatppuccinColors.() -> Color = when (accent) {
        "rosewater" -> CatppuccinColors::rosewater
        "flamingo" -> CatppuccinColors::flamingo
        "pink" -> CatppuccinColors::pink
        "mauve" -> CatppuccinColors::mauve
        "red" -> CatppuccinColors::red
        "maroon" -> CatppuccinColors::maroon
        "peach" -> CatppuccinColors::peach
        "yellow" -> CatppuccinColors::yellow
        "green" -> CatppuccinColors::green
        "teal" -> CatppuccinColors::teal
        "sky" -> CatppuccinColors::sky
        "sapphire" -> CatppuccinColors::sapphire
        "blue" -> CatppuccinColors::blue
        "lavender" -> CatppuccinColors::lavender
        else -> {
            { parseColor(accent) }
        }
    }

    CompositionLocalProvider(
        LocalCatppuccinColors provides colors,
    ) {
        MaterialTheme(
            colorScheme = createVercorsColorScheme(colors, accentProvider),
            typography = VercorsTypography,
            content = content
        )
    }
}

@ColorInt
fun parseColor(@Size(min = 1) colorString: String): Color {
    if (colorString[0] == '#') { // Use a long to avoid rollovers on #ffXXXXXX
        var color = colorString.substring(1).toLong(16)
        if (colorString.length == 7) { // Set the alpha value
            color = color or -0x1000000
        } else require(colorString.length == 9) { "Unknown color" }
        return Color(color.toInt())
    }
    throw IllegalArgumentException("Unknown color")
}

private val VercorsTypography: Typography @Composable get() {
    val fontFamily = FontFamily(
        Font(
            resource = CoreFont.InterRegular,
            weight = FontWeight.Normal,
        ),
        Font(
            resource = CoreFont.InterMedium,
            weight = FontWeight.Medium,
        ),
        Font(
            resource = CoreFont.InterBold,
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

@Stable
private fun createVercorsColorScheme(colors: CatppuccinColors, accentProvider: CatppuccinColors.() -> Color) =
    with(colors) {
    ColorScheme(
        primary = colors.accentProvider(),
        onPrimary = mantle,
        primaryContainer = lerp(base, colors.accentProvider(), 0.2f),
        onPrimaryContainer = text,
        inversePrimary = lerp(text, colors.accentProvider(), 0.2f),
        secondary = colors.accentProvider(),
        onSecondary = mantle,
        secondaryContainer = lerp(base, colors.accentProvider(), 0.2f),
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
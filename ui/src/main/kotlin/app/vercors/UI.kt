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

package app.vercors

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercors.common.AppColoredPalette
import app.vercors.common.AppPalette
import app.vercors.configuration.ConfigurationData
import app.vercors.di.DI
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import org.jetbrains.compose.resources.Font
import org.ocpsoft.prettytime.PrettyTime
import vercors.ui.generated.resources.*
import java.time.Instant
import javax.swing.SwingUtilities

object UI {
    val defaultRoundedCornerShape = RoundedCornerShape(5.dp)
    val largeRoundedCornerShape = RoundedCornerShape(10.dp)
    val normalLineHeight = 16.sp
    val mediumIconSize = 16.dp
    val largePadding = 20.dp
    val mediumLargePadding = 15.dp
    val mediumPadding = 10.dp
    val smallPadding = 5.dp
    val darker = Color(0x3f000000)
}

private val PrettyTime = PrettyTime()

@Stable
fun Instant.readable(): String = PrettyTime.format(this)

fun LazyGridScope.header(
    key: Any?,
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(key = key, span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}

@Composable
fun imageRequest(key: String, url: String): ImageRequest =
    ImageRequest.Builder(LocalPlatformContext.current)
        .data(url)
        .memoryCacheKey(key)
        .diskCacheKey(key)
        .build()

@Stable
fun Long.color(): Color = Color(this)

val AppColors: Colors
    @Composable get() {
        val palette = LocalPalette.current
        val accent = LocalConfiguration.current.accentColor.ofPalette(palette.original).color()
        return if (palette.original.isDark) darkColors(
            primary = accent,
            primaryVariant = accent,
            secondary = accent,
            secondaryVariant = accent,
            background = palette.base,
            surface = palette.surface0,
            error = palette.red
        ) else lightColors(
            primary = accent,
            primaryVariant = accent,
            secondary = accent,
            secondaryVariant = accent,
            background = palette.base,
            surface = palette.surface0,
            error = palette.red
        )
    }

private val AppFontFamily: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.Inter_Black, FontWeight.Black),
        Font(Res.font.Inter_Bold, FontWeight.Bold),
        Font(Res.font.Inter_ExtraBold, FontWeight.ExtraBold),
        Font(Res.font.Inter_ExtraLight, FontWeight.ExtraLight),
        Font(Res.font.Inter_Light, FontWeight.Light),
        Font(Res.font.Inter_Medium, FontWeight.Medium),
        Font(Res.font.Inter_Regular),
        Font(Res.font.Inter_SemiBold, FontWeight.SemiBold),
        Font(Res.font.Inter_Thin, FontWeight.Thin),
        Font(Res.font.Inter_BlackItalic, FontWeight.Black, FontStyle.Italic),
        Font(Res.font.Inter_BoldItalic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.Inter_ExtraBoldItalic, FontWeight.ExtraBold, FontStyle.Italic),
        Font(Res.font.Inter_ExtraLightItalic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(Res.font.Inter_LightItalic, FontWeight.Light, FontStyle.Italic),
        Font(Res.font.Inter_MediumItalic, FontWeight.Medium, FontStyle.Italic),
        Font(Res.font.Inter_Italic, style = FontStyle.Italic),
        Font(Res.font.Inter_SemiBoldItalic, FontWeight.SemiBold, FontStyle.Italic),
        Font(Res.font.Inter_ThinItalic, FontWeight.Thin, FontStyle.Italic)
    )

val AppTypography: Typography
    @Composable get() = Typography(
        defaultFontFamily = AppFontFamily,
        /*h1 = TextStyle.Default.copy(
            fontWeight = FontWeight.Light,
            fontSize = 96.sp,
            lineHeight = 112.sp,
            letterSpacing = (-1.5).sp
        ),
        h2 = TextStyle.Default.copy(
            fontWeight = FontWeight.Light,
            fontSize = 60.sp,
            lineHeight = 72.sp,
            letterSpacing = (-0.5).sp
        ),
        h3 = TextStyle.Default.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 48.sp,
            lineHeight = 56.sp,
            letterSpacing = 0.sp
        ),
        h4 = TextStyle.Default.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 34.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.25.sp
        ),*/
        h5 = TextStyle.Default.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        h6 = TextStyle.Default.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        subtitle1 = TextStyle.Default.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        subtitle2 = TextStyle.Default.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        body1 = TextStyle.Default.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.1.sp
        ),
        body2 = TextStyle.Default.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        button = TextStyle.Default.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.sp
        ),
        caption = TextStyle.Default.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        ),
        overline = TextStyle.Default.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        )
    )

val LocalConfiguration = compositionLocalOf { ConfigurationData() }
val LocalPalette = compositionLocalOf { AppColoredPalette(AppPalette.Macchiato) }
val LocalImageLoader = compositionLocalOf { ImageLoader.Builder(PlatformContext.INSTANCE).build() }
val LocalDI = compositionLocalOf { DI {} }

package com.skyecodes.vercors.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skyecodes.vercors.data.model.app.Configuration

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

    private object Font {
        val family = FontFamily(
            Font("font/inter/Inter-Black.ttf", FontWeight.Black),
            Font("font/inter/Inter-Bold.ttf", FontWeight.Bold),
            Font("font/inter/Inter-ExtraBold.ttf", FontWeight.ExtraBold),
            Font("font/inter/Inter-ExtraLight.ttf", FontWeight.ExtraLight),
            Font("font/inter/inter/Inter-Light.ttf", FontWeight.Light),
            Font("font/inter/Inter-Medium.ttf", FontWeight.Medium),
            Font("font/inter/Inter-Regular.ttf"),
            Font("font/inter/Inter-SemiBold.ttf", FontWeight.SemiBold),
            Font("font/inter/Inter-Thin.ttf", FontWeight.Thin),
            Font("font/inter/Inter-BlackItalic.ttf", FontWeight.Black, FontStyle.Italic),
            Font("font/inter/Inter-BoldItalic.ttf", FontWeight.Bold, FontStyle.Italic),
            Font("font/inter/Inter-ExtraBoldItalic.ttf", FontWeight.ExtraBold, FontStyle.Italic),
            Font("font/inter/Inter-ExtraLightItalic.ttf", FontWeight.ExtraLight, FontStyle.Italic),
            Font("font/inter/Inter-LightItalic.ttf", FontWeight.Light, FontStyle.Italic),
            Font("font/inter/Inter-MediumItalic.ttf", FontWeight.Medium, FontStyle.Italic),
            Font("font/inter/Inter-Italic.ttf", style = FontStyle.Italic),
            Font("font/inter/Inter-SemiBoldItalic.ttf", FontWeight.SemiBold, FontStyle.Italic),
            Font("font/inter/Inter-ThinItalic.ttf", FontWeight.Thin, FontStyle.Italic)
        )
    }

    val typography = Typography(
        defaultFontFamily = Font.family,
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
}

val LocalConfiguration = compositionLocalOf { Configuration.DEFAULT }

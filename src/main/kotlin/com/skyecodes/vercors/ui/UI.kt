package com.skyecodes.vercors.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skyecodes.vercors.data.model.app.Configuration

object UI {
    val defaultRoundedCornerShape = RoundedCornerShape(5.dp)
    val largeRoundedCornerShape = RoundedCornerShape(15.dp)
    val titleFontSize = 20.sp
    val subtitleFontSize = 16.sp
    val smallFontSize = 10.sp
    val mediumIconSize = 16.dp
    val smallIconSize = 12.dp
    val largePadding = 20.dp
    val mediumLargePadding = 15.dp
    val mediumPadding = 10.dp
    val smallPadding = 5.dp

    const val vanilla = "Vanilla"

    private object Font {
        val family = FontFamily(
            Font("font/inter/Inter-Light.ttf", FontWeight.Light),
            Font("font/inter/Inter-Medium.ttf", FontWeight.Medium),
            Font("font/inter/Inter-Regular.ttf"),
        )
    }

    val typography = Typography(
        defaultFontFamily = Font.family,
        button = TextStyle.Default.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp
        )
    )
}

val LocalConfiguration = compositionLocalOf { Configuration.DEFAULT }

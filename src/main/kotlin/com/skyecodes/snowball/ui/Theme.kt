package com.skyecodes.snowball.ui

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

object Theme {
    var currentPalette: Palette = Mocha

    interface Palette {
        val rosewater: Color
        val flamingo: Color
        val pink: Color
        val mauve: Color
        val red: Color
        val maroon: Color
        val peach: Color
        val yellow: Color
        val green: Color
        val teal: Color
        val sky: Color
        val sapphire: Color
        val blue: Color
        val lavender: Color
        val text: Color
        val subtext1: Color
        val subtext0: Color
        val overlay2: Color
        val overlay1: Color
        val overlay0: Color
        val surface2: Color
        val surface1: Color
        val surface0: Color
        val base: Color
        val mantle: Color
        val crust: Color

        val colors: Colors
    }

    object Latte : Palette {
        override val rosewater = Color(0xff)
        override val flamingo = Color(0xff)
        override val pink = Color(0xff)
        override val mauve = Color(0xff)
        override val red = Color(0xff)
        override val maroon = Color(0xff)
        override val peach = Color(0xff)
        override val yellow = Color(0xff)
        override val green = Color(0xff)
        override val teal = Color(0xff)
        override val sky = Color(0xff)
        override val sapphire = Color(0xff)
        override val blue = Color(0xff)
        override val lavender = Color(0xff)
        override val text = Color(0xff)
        override val subtext1 = Color(0xff)
        override val subtext0 = Color(0xff)
        override val overlay2 = Color(0xff)
        override val overlay1 = Color(0xff)
        override val overlay0 = Color(0xff)
        override val surface2 = Color(0xff)
        override val surface1 = Color(0xff)
        override val surface0 = Color(0xff)
        override val base = Color(0xff)
        override val mantle = Color(0xff)
        override val crust = Color(0xff)

        override val colors: Colors
            get() = TODO("Not yet implemented")
    }

    object Mocha : Palette {
        override val rosewater = Color(0xfff5e0dc)
        override val flamingo = Color(0xfff2cdcd)
        override val pink = Color(0xfff5c2e7)
        override val mauve = Color(0xffcba6f7)
        override val red = Color(0xfff38ba8)
        override val maroon = Color(0xffeba0ac)
        override val peach = Color(0xfffab387)
        override val yellow = Color(0xfff9e2af)
        override val green = Color(0xffa6e3a1)
        override val teal = Color(0xff94e2d5)
        override val sky = Color(0xff89dceb)
        override val sapphire = Color(0xff74c7ec)
        override val blue = Color(0xff89b4fa)
        override val lavender = Color(0xffb4befe)
        override val text = Color(0xffcdd6f4)
        override val subtext1 = Color(0xffbac2de)
        override val subtext0 = Color(0xffa6adc8)
        override val overlay2 = Color(0xff9399b2)
        override val overlay1 = Color(0xff7f849c)
        override val overlay0 = Color(0xff6c7086)
        override val surface2 = Color(0xff585b70)
        override val surface1 = Color(0xff45475a)
        override val surface0 = Color(0xff313244)
        override val base = Color(0xff1e1e2e)
        override val mantle = Color(0xff181825)
        override val crust = Color(0xff11111b)

        override val colors = Colors(
            primary = mauve,
            primaryVariant = blue,
            secondary = teal,
            secondaryVariant = sky,
            background = base,
            surface = surface0,
            error = red,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
            onError = Color.White,
            isLight = false
        )
    }

    object Font {
        val regular = Font(
            resource = "font/Inter-Regular.ttf",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )

        val bold = Font(
            resource = "font/Inter-Bold.ttf",
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )

        val family = FontFamily(regular, bold)
    }


}
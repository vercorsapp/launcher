package com.skyecodes.vercors.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
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

    object Text {
        // General
        const val HOME = "Home"
        const val INSTANCES = "Instances"
        const val SEARCH = "Search"
        const val ACCOUNTS = "Accounts"
        const val SETTINGS = "Settings"
        const val CANCEL = "Cancel"
        const val CLOSE = "Close"

        // Home
        const val PLAY = "Play"
        const val VIEW = "View"
        const val INSTALL = "Install"

        // Instances
        const val NO_INSTANCES_FOUND = "No instances found"
        const val CREATE_NEW_INSTANCE = "Create new instance"
        const val INSTANCE_NAME = "Instance name"
        const val ICON = "Icon"
        const val MINECRAFT_VERSION = "Minecraft version"
        const val INCLUDE_SNAPSHOTS = "Include snapshots"
        const val LOADER = "Loader"
        const val LOADER_VERSION = "Loader version"
        const val CREATE = "Create"

        // Settings
        const val USER_INTERFACE = "User interface"
        const val THEME = "Theme"
        const val THEME_DESCRIPTION = "Change the global theme of the launcher."
        const val ACCENT_COLOR = "Accent color"
        const val ACCENT_COLOR_DESCRIPTION = "Change the accent color used in some buttons."
        const val SYSTEM_WINDOW = "Use system window frame"
        const val SYSTEM_WINDOW_DESCRIPTION =
            "Use the system window frame instead of the custom window frame provided by the launcher."
        const val ANIMATIONS = "Animations"
        const val ANIMATIONS_DESCRIPTION = "Enables UI animations such as fade in, smooth element size change, etc."
        const val DEFAULT_TAB = "Default tab"
        const val DEFAULT_TAB_DESCRIPTION = "Change the tab that shows up when the launcher is opened."
        const val PROVIDERS = "Providers"
        const val PROVIDERS_DESCRIPTION =
            "Choose which service(s) provide popular projects on the home tab."
    }

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

        val transparentOverlay: Color
        fun material(accent: Color): Colors
    }

    object Latte : Palette {
        override val rosewater = Color(0xffdc8a78)
        override val flamingo = Color(0xffdd7878)
        override val pink = Color(0xffea76cb)
        override val mauve = Color(0xff8839ef)
        override val red = Color(0xffd20f39)
        override val maroon = Color(0xffe64553)
        override val peach = Color(0xfffe640b)
        override val yellow = Color(0xffdf8e1d)
        override val green = Color(0xff40a02b)
        override val teal = Color(0xff179299)
        override val sky = Color(0xff04a5e5)
        override val sapphire = Color(0xff209fb5)
        override val blue = Color(0xff1e66f5)
        override val lavender = Color(0xff7287fd)
        override val text = Color(0xff4c4f69)
        override val subtext1 = Color(0xff5c5f77)
        override val subtext0 = Color(0xff6c6f85)
        override val overlay2 = Color(0xff7c7f93)
        override val overlay1 = Color(0xff8c8fa1)
        override val overlay0 = Color(0xff9ca0b0)
        override val surface2 = Color(0xffacb0be)
        override val surface1 = Color(0xffbcc0cc)
        override val surface0 = Color(0xffccd0da)
        override val base = Color(0xffeff1f5)
        override val mantle = Color(0xffe6e9ef)
        override val crust = Color(0xffdce0e8)

        override val transparentOverlay = Color(0x7f000000)
        override fun material(accent: Color) = lightColors(
            primary = accent,
            primaryVariant = accent,
            secondary = accent,
            secondaryVariant = accent,
            background = base,
            surface = surface0,
            error = red
        )
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

        override val transparentOverlay = Color(0xbf000000)
        override fun material(accent: Color) = darkColors(
            primary = accent,
            primaryVariant = accent,
            secondary = accent,
            secondaryVariant = accent,
            background = base,
            surface = surface0,
            error = red
        )
    }

    object Font {

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

    val transitionIn = fadeIn(spring(stiffness = Spring.StiffnessMedium))
    val transitionOut = fadeOut(spring(stiffness = Spring.StiffnessMedium))
}

val LocalPalette = compositionLocalOf<UI.Palette> { UI.Mocha }
val LocalConfiguration = compositionLocalOf { Configuration.DEFAULT }

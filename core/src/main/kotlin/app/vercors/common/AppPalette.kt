package app.vercors.common

sealed interface AppPalette {
    val rosewater: Long
    val flamingo: Long
    val pink: Long
    val mauve: Long
    val red: Long
    val maroon: Long
    val peach: Long
    val yellow: Long
    val green: Long
    val teal: Long
    val sky: Long
    val sapphire: Long
    val blue: Long
    val lavender: Long
    val text: Long
    val subtext1: Long
    val subtext0: Long
    val overlay2: Long
    val overlay1: Long
    val overlay0: Long
    val surface2: Long
    val surface1: Long
    val surface0: Long
    val base: Long
    val mantle: Long
    val crust: Long
    val transparentOverlay: Long
    val isDark: Boolean

    data object Latte : AppPalette {
        override val rosewater = 0xffdc8a78
        override val flamingo = 0xffdd7878
        override val pink = 0xffea76cb
        override val mauve = 0xff8839ef
        override val red = 0xffd20f39
        override val maroon = 0xffe64553
        override val peach = 0xfffe640b
        override val yellow = 0xffdf8e1d
        override val green = 0xff40a02b
        override val teal = 0xff179299
        override val sky = 0xff04a5e5
        override val sapphire = 0xff209fb5
        override val blue = 0xff1e66f5
        override val lavender = 0xff7287fd
        override val text = 0xff4c4f69
        override val subtext1 = 0xff5c5f77
        override val subtext0 = 0xff6c6f85
        override val overlay2 = 0xff7c7f93
        override val overlay1 = 0xff8c8fa1
        override val overlay0 = 0xff9ca0b0
        override val surface2 = 0xffacb0be
        override val surface1 = 0xffbcc0cc
        override val surface0 = 0xffccd0da
        override val base = 0xffeff1f5
        override val mantle = 0xffe6e9ef
        override val crust = 0xffdce0e8
        override val transparentOverlay: Long = 0x7f000000
        override val isDark = false
    }

    data object Frappe : AppPalette {
        override val rosewater = 0xfff2d5cf
        override val flamingo = 0xffeebebe
        override val pink = 0xfff4b8e4
        override val mauve = 0xffca9ee6
        override val red = 0xffe78284
        override val maroon = 0xffea999c
        override val peach = 0xffef9f76
        override val yellow = 0xffe5c890
        override val green = 0xffa6d189
        override val teal = 0xff81c8be
        override val sky = 0xff99d1db
        override val sapphire = 0xff85c1dc
        override val blue = 0xff8caaee
        override val lavender = 0xffbabbf1
        override val text = 0xffc6d0f5
        override val subtext1 = 0xffb5bfe2
        override val subtext0 = 0xffa5adce
        override val overlay2 = 0xff949cbb
        override val overlay1 = 0xff838ba7
        override val overlay0 = 0xff737994
        override val surface2 = 0xff626880
        override val surface1 = 0xff51576d
        override val surface0 = 0xff414559
        override val base = 0xff303446
        override val mantle = 0xff292c3c
        override val crust = 0xff232634
        override val transparentOverlay = 0xbf000000
        override val isDark = true
    }

    data object Macchiato : AppPalette {
        override val rosewater = 0xfff4dbd6
        override val flamingo = 0xfff0c6c6
        override val pink = 0xfff5bde6
        override val mauve = 0xffc6a0f6
        override val red = 0xffed8796
        override val maroon = 0xffee99a0
        override val peach = 0xfff5a97f
        override val yellow = 0xffeed49f
        override val green = 0xffa6da95
        override val teal = 0xff8bd5ca
        override val sky = 0xff91d7e3
        override val sapphire = 0xff7dc4e4
        override val blue = 0xff8aadf4
        override val lavender = 0xffb7bdf8
        override val text = 0xffcad3f5
        override val subtext1 = 0xffb8c0e0
        override val subtext0 = 0xffa5adcb
        override val overlay2 = 0xff939ab7
        override val overlay1 = 0xff8087a2
        override val overlay0 = 0xff6e738d
        override val surface2 = 0xff5b6078
        override val surface1 = 0xff494d64
        override val surface0 = 0xff363a4f
        override val base = 0xff24273a
        override val mantle = 0xff1e2030
        override val crust = 0xff181926
        override val transparentOverlay = 0xbf000000
        override val isDark = true
    }

    data object Mocha : AppPalette {
        override val rosewater = 0xfff5e0dc
        override val flamingo = 0xfff2cdcd
        override val pink = 0xfff5c2e7
        override val mauve = 0xffcba6f7
        override val red = 0xfff38ba8
        override val maroon = 0xffeba0ac
        override val peach = 0xfffab387
        override val yellow = 0xfff9e2af
        override val green = 0xffa6e3a1
        override val teal = 0xff94e2d5
        override val sky = 0xff89dceb
        override val sapphire = 0xff74c7ec
        override val blue = 0xff89b4fa
        override val lavender = 0xffb4befe
        override val text = 0xffcdd6f4
        override val subtext1 = 0xffbac2de
        override val subtext0 = 0xffa6adc8
        override val overlay2 = 0xff9399b2
        override val overlay1 = 0xff7f849c
        override val overlay0 = 0xff6c7086
        override val surface2 = 0xff585b70
        override val surface1 = 0xff45475a
        override val surface0 = 0xff313244
        override val base = 0xff1e1e2e
        override val mantle = 0xff181825
        override val crust = 0xff11111b
        override val transparentOverlay = 0xbf000000
        override val isDark = true
    }
}
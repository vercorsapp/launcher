/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.launcher.core.presentation.theme

import androidx.compose.ui.graphics.Color

data class CatppuccinColors(
    val raw: CatppuccinRawColors
) {
    val rosewater = Color(raw.rosewater)
    val flamingo = Color(raw.flamingo)
    val pink = Color(raw.pink)
    val mauve = Color(raw.mauve)
    val red = Color(raw.red)
    val maroon = Color(raw.maroon)
    val peach = Color(raw.peach)
    val yellow = Color(raw.yellow)
    val green = Color(raw.green)
    val teal = Color(raw.teal)
    val sky = Color(raw.sky)
    val sapphire = Color(raw.sapphire)
    val blue = Color(raw.blue)
    val lavender = Color(raw.lavender)
    val text = Color(raw.text)
    val subtext1 = Color(raw.subtext1)
    val subtext0 = Color(raw.subtext0)
    val overlay2 = Color(raw.overlay2)
    val overlay1 = Color(raw.overlay1)
    val overlay0 = Color(raw.overlay0)
    val surface2 = Color(raw.surface2)
    val surface1 = Color(raw.surface1)
    val surface0 = Color(raw.surface0)
    val base = Color(raw.base)
    val mantle = Color(raw.mantle)
    val crust = Color(raw.crust)
    val transparentOverlay = Color(raw.transparentOverlay)
    val isDark = raw.isDark

    companion object {
        val Latte = CatppuccinColors(CatppuccinRawColors.Latte)
        val Frappe = CatppuccinColors(CatppuccinRawColors.Frappe)
        val Macchiato = CatppuccinColors(CatppuccinRawColors.Macchiato)
        val Mocha = CatppuccinColors(CatppuccinRawColors.Mocha)
    }
}

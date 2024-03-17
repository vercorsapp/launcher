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

package app.vercors.common

import app.vercors.color

class AppColoredPalette(val original: AppPalette) {
    val rosewater = original.rosewater.color()
    val flamingo = original.flamingo.color()
    val pink = original.pink.color()
    val mauve = original.mauve.color()
    val red = original.red.color()
    val maroon = original.maroon.color()
    val peach = original.peach.color()
    val yellow = original.yellow.color()
    val green = original.green.color()
    val teal = original.teal.color()
    val sky = original.sky.color()
    val sapphire = original.sapphire.color()
    val blue = original.blue.color()
    val lavender = original.lavender.color()
    val text = original.text.color()
    val subtext1 = original.subtext1.color()
    val subtext0 = original.subtext0.color()
    val overlay2 = original.overlay2.color()
    val overlay1 = original.overlay1.color()
    val overlay0 = original.overlay0.color()
    val surface2 = original.surface2.color()
    val surface1 = original.surface1.color()
    val surface0 = original.surface0.color()
    val base = original.base.color()
    val mantle = original.mantle.color()
    val crust = original.crust.color()
    val transparentOverlay = original.transparentOverlay.color()
}
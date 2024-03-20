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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppColor(val ofPalette: (AppPalette) -> Long) {
    @SerialName("rosewater")
    Rosewater(AppPalette::rosewater),

    @SerialName("flamingo")
    Flamingo(AppPalette::flamingo),

    @SerialName("pink")
    Pink(AppPalette::pink),

    @SerialName("mauve")
    Mauve(AppPalette::mauve),

    @SerialName("red")
    Red(AppPalette::red),

    @SerialName("maroon")
    Maroon(AppPalette::maroon),

    @SerialName("peach")
    Peach(AppPalette::peach),

    @SerialName("yellow")
    Yellow(AppPalette::yellow),

    @SerialName("green")
    Green(AppPalette::green),

    @SerialName("teal")
    Teal(AppPalette::teal),

    @SerialName("sky")
    Sky(AppPalette::sky),

    @SerialName("sapphire")
    Sapphire(AppPalette::sapphire),

    @SerialName("blue")
    Blue(AppPalette::blue),

    @SerialName("lavender")
    Lavender(AppPalette::lavender);
}

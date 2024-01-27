package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.Color
import com.skyecodes.vercors.ui.Palette
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppColor(val title: String, val ofPalette: (Palette) -> Color) {
    @SerialName("rosewater")
    Rosewater("Rosewater", Palette::rosewater),

    @SerialName("flamingo")
    Flamingo("Flamingo", Palette::flamingo),

    @SerialName("pink")
    Pink("Pink", Palette::pink),

    @SerialName("mauve")
    Mauve("Mauve", Palette::mauve),

    @SerialName("red")
    Red("Red", Palette::red),

    @SerialName("maroon")
    Maroon("Maroon", Palette::maroon),

    @SerialName("peach")
    Peach("Peach", Palette::peach),

    @SerialName("yellow")
    Yellow("Yellow", Palette::yellow),

    @SerialName("green")
    Green("Green", Palette::green),

    @SerialName("teal")
    Teal("Teal", Palette::teal),

    @SerialName("sky")
    Sky("Sky", Palette::sky),

    @SerialName("sapphire")
    Sapphire("Sapphire", Palette::sapphire),

    @SerialName("blue")
    Blue("Blue", Palette::blue),

    @SerialName("lavender")
    Lavender("Lavender", Palette::lavender);
}

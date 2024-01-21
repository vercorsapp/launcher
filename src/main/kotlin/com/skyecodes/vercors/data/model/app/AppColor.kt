package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.Color
import com.skyecodes.vercors.ui.UI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppColor(val title: String, val ofPalette: (UI.Palette) -> Color) {
    @SerialName("rosewater")
    Rosewater("Rosewater", UI.Palette::rosewater),

    @SerialName("flamingo")
    Flamingo("Flamingo", UI.Palette::flamingo),

    @SerialName("pink")
    Pink("Pink", UI.Palette::pink),

    @SerialName("mauve")
    Mauve("Mauve", UI.Palette::mauve),

    @SerialName("red")
    Red("Red", UI.Palette::red),

    @SerialName("maroon")
    Maroon("Maroon", UI.Palette::maroon),

    @SerialName("peach")
    Peach("Peach", UI.Palette::peach),

    @SerialName("yellow")
    Yellow("Yellow", UI.Palette::yellow),

    @SerialName("green")
    Green("Green", UI.Palette::green),

    @SerialName("teal")
    Teal("Teal", UI.Palette::teal),

    @SerialName("sky")
    Sky("Sky", UI.Palette::sky),

    @SerialName("sapphire")
    Sapphire("Sapphire", UI.Palette::sapphire),

    @SerialName("blue")
    Blue("Blue", UI.Palette::blue),

    @SerialName("lavender")
    Lavender("Lavender", UI.Palette::lavender);
}

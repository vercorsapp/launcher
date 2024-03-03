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

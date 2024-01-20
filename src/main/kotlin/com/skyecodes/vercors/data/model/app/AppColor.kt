package com.skyecodes.vercors.data.model.app

import androidx.compose.ui.graphics.Color
import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import com.skyecodes.vercors.ui.UI
import kotlinx.serialization.Serializable
import java.util.*

@Serializable(AppColorSerializer::class)
enum class AppColor(override val value: String, val color: (UI.Palette) -> Color) : StringEnumerable {
    Rosewater("rosewater", UI.Palette::rosewater),
    Flamingo("flamingo", UI.Palette::flamingo),
    Pink("pink", UI.Palette::pink),
    Mauve("mauve", UI.Palette::mauve),
    Red("red", UI.Palette::red),
    Maroon("maroon", UI.Palette::maroon),
    Peach("peach", UI.Palette::peach),
    Yellow("yellow", UI.Palette::yellow),
    Green("green", UI.Palette::green),
    Teal("teal", UI.Palette::teal),
    Sky("sky", UI.Palette::sky),
    Sapphire("sapphire", UI.Palette::sapphire),
    Blue("blue", UI.Palette::blue),
    Lavender("lavender", UI.Palette::lavender);

    val title: String =
        value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

private class AppColorSerializer : StringEnumerableSerializer<AppColor>(AppColor.entries)

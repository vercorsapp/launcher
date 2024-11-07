package app.vercors.launcher.settings.presentation.state

import androidx.compose.ui.graphics.Color
import app.vercors.launcher.core.presentation.theme.CatppuccinColors

data class AccentColorUi(
    val id: String,
    val name: String,
    val color: (CatppuccinColors) -> Color
) {
    companion object {
        val DEFAULT_LIST = listOf(
            AccentColorUi("rosewater", "Rosewater", CatppuccinColors::rosewater),
            AccentColorUi("flamingo", "Flamingo", CatppuccinColors::flamingo),
            AccentColorUi("pink", "Pink", CatppuccinColors::pink),
            AccentColorUi("mauve", "Mauve", CatppuccinColors::mauve),
            AccentColorUi("red", "Red", CatppuccinColors::red),
            AccentColorUi("maroon", "Maroon", CatppuccinColors::maroon),
            AccentColorUi("peach", "Peach", CatppuccinColors::peach),
            AccentColorUi("yellow", "Yellow", CatppuccinColors::yellow),
            AccentColorUi("green", "Green", CatppuccinColors::green),
            AccentColorUi("teal", "Teal", CatppuccinColors::teal),
            AccentColorUi("sky", "Sky", CatppuccinColors::sky),
            AccentColorUi("sapphire", "Sapphire", CatppuccinColors::sapphire),
            AccentColorUi("blue", "Blue", CatppuccinColors::blue),
            AccentColorUi("lavender", "Lavender", CatppuccinColors::lavender),
        )
    }
}

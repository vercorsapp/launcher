package app.vercors.launcher.settings.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.presentation.theme.CatppuccinColors

@Immutable
sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Loaded(
        val config: AppConfig,
        val themes: List<UiTheme> = UiTheme.DEFAULT_LIST,
        val accentColors: List<UiAccentColor> = UiAccentColor.DEFAULT_LIST
    ) : SettingsUiState {
        val currentTheme: UiTheme = themes.first { it.id == config.general.theme }
        val currentAccentColor: UiAccentColor = accentColors.first { it.id == config.general.accent }
    }
}

data class UiAccentColor(
    val id: String,
    val name: String,
    val color: (CatppuccinColors) -> Color
) {
    companion object {
        val DEFAULT_LIST = listOf(
            UiAccentColor("rosewater", "Rosewater", CatppuccinColors::rosewater),
            UiAccentColor("flamingo", "Flamingo", CatppuccinColors::flamingo),
            UiAccentColor("pink", "Pink", CatppuccinColors::pink),
            UiAccentColor("mauve", "Mauve", CatppuccinColors::mauve),
            UiAccentColor("red", "Red", CatppuccinColors::red),
            UiAccentColor("maroon", "Maroon", CatppuccinColors::maroon),
            UiAccentColor("peach", "Peach", CatppuccinColors::peach),
            UiAccentColor("yellow", "Yellow", CatppuccinColors::yellow),
            UiAccentColor("green", "Green", CatppuccinColors::green),
            UiAccentColor("teal", "Teal", CatppuccinColors::teal),
            UiAccentColor("sky", "Sky", CatppuccinColors::sky),
            UiAccentColor("sapphire", "Sapphire", CatppuccinColors::sapphire),
            UiAccentColor("blue", "Blue", CatppuccinColors::blue),
            UiAccentColor("lavender", "Lavender", CatppuccinColors::lavender),
        )
    }
}

data class UiTheme(
    val id: String,
    val name: String,
) {
    companion object {
        val DEFAULT_LIST = listOf(
            UiTheme("catppuccin-latte", "Catppuccin Latte"),
            UiTheme("catppuccin-frappe", "Catppuccin Frappe"),
            UiTheme("catppuccin-macchiato", "Catppuccin Macchiato"),
            UiTheme("catppuccin-mocha", "Catppuccin Mocha"),
        )
    }
}

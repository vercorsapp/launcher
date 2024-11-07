package app.vercors.launcher.settings.presentation.state

data class ThemeUi(
    val id: String,
    val name: String,
) {
    companion object {
        val DEFAULT_LIST = listOf(
            ThemeUi("catppuccin-latte", "Catppuccin Latte"),
            ThemeUi("catppuccin-frappe", "Catppuccin Frappe"),
            ThemeUi("catppuccin-macchiato", "Catppuccin Macchiato"),
            ThemeUi("catppuccin-mocha", "Catppuccin Mocha"),
        )
    }
}

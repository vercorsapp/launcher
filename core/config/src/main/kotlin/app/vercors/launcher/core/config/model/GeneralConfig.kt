package app.vercors.launcher.core.config.model

data class GeneralConfig(
    val theme: String,
    val accent: String,
    val decorated: Boolean,
    val animations: Boolean,
    val defaultTab: TabConfig
) {
    companion object {
        val DEFAULT = GeneralConfig(
            theme = "catppuccin-mocha",
            accent = "mauve",
            decorated = false,
            animations = true,
            defaultTab = TabConfig.Home
        )
    }
}

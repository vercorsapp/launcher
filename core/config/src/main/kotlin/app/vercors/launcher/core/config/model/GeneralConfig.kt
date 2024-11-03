package app.vercors.launcher.core.config.model

data class GeneralConfig(
    val theme: String,
    val accent: String,
    val decorated: Boolean,
    val animations: Boolean,
    val defaultTab: TabConfig
)

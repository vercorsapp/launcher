package app.vercors.launcher.core.config.model

data class AppConfig(
    val general: GeneralConfig,
    val home: HomeConfig,
) {
    companion object {
        val DEFAULT = AppConfig(
            general = GeneralConfig(
                theme = "mocha",
                accent = "mauve",
                decorated = false,
                animations = true,
                defaultTab = TabConfig.Home
            ),
            home = HomeConfig(
                sections = HomeSectionConfig.entries.toList(),
                provider = HomeProviderConfig.Modrinth
            )
        )
    }
}
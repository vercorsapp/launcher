package app.vercors.launcher.core.config.model

data class HomeConfig(
    val sections: List<HomeSectionConfig>,
    val provider: HomeProviderConfig
) {
    companion object {
        val DEFAULT = HomeConfig(
            sections = HomeSectionConfig.entries.toList(),
            provider = HomeProviderConfig.Modrinth
        )
    }
}

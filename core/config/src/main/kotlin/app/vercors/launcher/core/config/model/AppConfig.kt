package app.vercors.launcher.core.config.model

data class AppConfig(
    val general: GeneralConfig,
    val home: HomeConfig,
) {
    companion object {
        val DEFAULT = AppConfig(
            general = GeneralConfig.DEFAULT,
            home = HomeConfig.DEFAULT
        )
    }
}
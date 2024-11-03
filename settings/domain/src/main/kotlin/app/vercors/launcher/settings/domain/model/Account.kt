package app.vercors.launcher.settings.domain.model

data class Account(
    val uuid: String,
    val username: String,
    val accessToken: String?,
    val refreshToken: String?
)
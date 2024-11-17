package app.vercors.launcher.game.domain.version

import kotlinx.datetime.Instant

data class GameVersion(
    val id: String,
    val type: GameVersionType,
    val url: String,
    val releaseDate: Instant,
    val sha1: String
)

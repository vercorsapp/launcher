package app.vercors.launcher.game.domain.version

data class GameVersionList(
    val latestReleaseId: String,
    val latestSnapshotId: String,
    val versions: List<GameVersion>
)
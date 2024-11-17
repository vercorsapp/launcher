package app.vercors.launcher.game.domain.version

data class GameVersionList(
    val latestReleaseId: String,
    val latestSnapshotId: String,
    val versions: List<GameVersion>
) {
    val latestRelease: GameVersion by lazy { versions.first { it.id == latestReleaseId } }
    val latestSnapshot: GameVersion by lazy { versions.first { it.id == latestSnapshotId } }
}
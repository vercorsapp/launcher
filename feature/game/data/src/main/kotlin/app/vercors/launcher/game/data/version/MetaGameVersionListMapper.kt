package app.vercors.launcher.game.data.version

import app.vercors.launcher.game.domain.version.GameVersion
import app.vercors.launcher.game.domain.version.GameVersionList
import app.vercors.launcher.game.domain.version.GameVersionType
import app.vercors.meta.game.MetaGameVersion
import app.vercors.meta.game.MetaGameVersionList
import app.vercors.meta.game.MetaGameVersionType
import kotlinx.datetime.Instant


fun MetaGameVersionList.toGameVersionList(): GameVersionList = GameVersionList(
    latestReleaseId = recommended,
    latestSnapshotId = latest,
    versions = this.versionsList.map { it.toGameVersion() }
)

fun MetaGameVersion.toGameVersion(): GameVersion = GameVersion(
    id = id,
    type = type.toGameVersionType(),
    url = url,
    releaseDate = Instant.fromEpochMilliseconds(time),
    sha1 = sha1,
)

fun MetaGameVersionType.toGameVersionType(): GameVersionType = when (this) {
    MetaGameVersionType.release -> GameVersionType.Release
    MetaGameVersionType.snapshot -> GameVersionType.Snapshot
    MetaGameVersionType.beta -> GameVersionType.Beta
    MetaGameVersionType.alpha -> GameVersionType.Alpha
    MetaGameVersionType.UNRECOGNIZED -> throw IllegalArgumentException("Unrecognized game version type")
}
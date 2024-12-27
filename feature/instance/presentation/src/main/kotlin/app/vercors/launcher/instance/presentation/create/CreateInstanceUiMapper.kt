package app.vercors.launcher.instance.presentation.create

import app.vercors.launcher.game.domain.loader.LoaderVersion
import app.vercors.launcher.game.domain.version.GameVersionList
import app.vercors.launcher.game.domain.version.GameVersionType

fun GameVersionList.toUi(): List<GameVersionUi> = versions.map {
    GameVersionUi(
        id = it.id,
        isSnapshot = it.type != GameVersionType.Release,
        isLatestRelease = it.id == latestReleaseId,
        isLatestSnapshot = it.id == latestSnapshotId
    )
}

fun List<LoaderVersion>.toUi(): List<String> = map { it.id }
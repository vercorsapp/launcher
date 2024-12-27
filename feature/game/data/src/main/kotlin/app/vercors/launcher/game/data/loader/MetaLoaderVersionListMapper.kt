package app.vercors.launcher.game.data.loader

import app.vercors.launcher.game.domain.loader.LoaderVersion
import app.vercors.meta.loader.MetaLoaderVersionList

fun MetaLoaderVersionList.toLoaderVersionList() = versionsList.map { LoaderVersion(it) }
package app.vercors.launcher.home.data

import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.project.domain.ProjectProvider

fun HomeProviderConfig.toType(): ProjectProvider = when (this) {
    HomeProviderConfig.Modrinth -> ProjectProvider.Modrinth
    HomeProviderConfig.Curseforge -> ProjectProvider.Curseforge
}
package app.vercors.launcher.home.data.mapper

import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.project.domain.model.ProjectProvider

fun HomeProviderConfig.toType(): ProjectProvider = when (this) {
    HomeProviderConfig.Modrinth -> ProjectProvider.Modrinth
    HomeProviderConfig.Curseforge -> ProjectProvider.Curseforge
}
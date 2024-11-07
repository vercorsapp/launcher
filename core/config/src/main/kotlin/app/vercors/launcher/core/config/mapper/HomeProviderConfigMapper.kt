package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.proto.HomeProviderProto

fun HomeProviderProto.toConfig(): HomeProviderConfig = when (this) {
    HomeProviderProto.MODRINTH -> HomeProviderConfig.Modrinth
    HomeProviderProto.CURSEFORGE -> HomeProviderConfig.Curseforge
    HomeProviderProto.UNRECOGNIZED -> HomeProviderConfig.entries.first()
}

fun HomeProviderConfig.toProto(): HomeProviderProto = when (this) {
    HomeProviderConfig.Modrinth -> HomeProviderProto.MODRINTH
    HomeProviderConfig.Curseforge -> HomeProviderProto.CURSEFORGE
}
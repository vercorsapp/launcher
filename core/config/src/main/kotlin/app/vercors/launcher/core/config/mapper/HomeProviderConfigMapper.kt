package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.proto.HomeProvider
import org.koin.core.annotation.Single

@Single
class HomeProviderConfigMapper {
    fun fromProto(provider: HomeProvider): HomeProviderConfig = when (provider) {
        HomeProvider.MODRINTH -> HomeProviderConfig.Modrinth
        HomeProvider.CURSEFORGE -> HomeProviderConfig.Curseforge
        HomeProvider.UNRECOGNIZED -> HomeProviderConfig.entries.first()
    }

    fun toProto(homeProviderConfig: HomeProviderConfig): HomeProvider = when (homeProviderConfig) {
        HomeProviderConfig.Modrinth -> HomeProvider.MODRINTH
        HomeProviderConfig.Curseforge -> HomeProvider.CURSEFORGE
    }
}

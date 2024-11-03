package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.HomeConfig
import app.vercors.launcher.core.config.proto.Home
import app.vercors.launcher.core.config.proto.home
import org.koin.core.annotation.Single

@Single
class HomeConfigMapper(
    private val homeSectionConfigMapper: HomeSectionConfigMapper,
    private val homeProviderConfigMapper: HomeProviderConfigMapper
) {
    fun fromProto(home: Home): HomeConfig = HomeConfig(
        sections = home.sectionsList.map { homeSectionConfigMapper.fromProto(it) },
        provider = homeProviderConfigMapper.fromProto(home.provider)
    )

    fun toProto(homeConfig: HomeConfig): Home = home {
        sections += homeConfig.sections.map { homeSectionConfigMapper.toProto(it) }
        provider = homeProviderConfigMapper.toProto(homeConfig.provider)
    }
}

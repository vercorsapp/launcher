package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.HomeConfig
import app.vercors.launcher.core.config.proto.HomeProto
import app.vercors.launcher.core.config.proto.homeProto

fun HomeProto.toConfig(): HomeConfig = HomeConfig(
    sections = sectionsList.map { it.toConfig() },
    provider = provider.toConfig()
)

fun HomeConfig.toProto(): HomeProto = homeProto {
    sections += this@toProto.sections.map { it.toProto() }
    provider = this@toProto.provider.toProto()
}
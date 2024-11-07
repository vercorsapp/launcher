package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.proto.ConfigProto
import app.vercors.launcher.core.config.proto.configProto

fun ConfigProto.toConfig(): AppConfig = AppConfig(
    general = general.toConfig(),
    home = home.toConfig()
)

fun AppConfig.toProto(): ConfigProto = configProto {
    general = this@toProto.general.toProto()
    home = this@toProto.home.toProto()
}
package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.config.proto.GeneralProto
import app.vercors.launcher.core.config.proto.generalProto

fun GeneralProto.toConfig(): GeneralConfig = GeneralConfig(
    theme = theme,
    accent = accent,
    decorated = decorated,
    animations = animations,
    defaultTab = defaultTab.toConfig()
)

fun GeneralConfig.toProto(): GeneralProto = generalProto {
    theme = this@toProto.theme
    accent = this@toProto.accent
    decorated = this@toProto.decorated
    animations = this@toProto.animations
    defaultTab = this@toProto.defaultTab.toProto()
}
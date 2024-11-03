package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.config.proto.General
import app.vercors.launcher.core.config.proto.general
import org.koin.core.annotation.Single

@Single
class GeneralConfigMapper(
    private val tabConfigMapper: TabConfigMapper
) {
    fun fromProto(general: General): GeneralConfig = GeneralConfig(
        theme = general.theme,
        accent = general.accent,
        decorated = general.decorated,
        animations = general.animations,
        defaultTab = tabConfigMapper.fromProto(general.defaultTab)
    )

    fun toProto(generalConfig: GeneralConfig): General = general {
        theme = generalConfig.theme
        accent = generalConfig.accent
        decorated = generalConfig.decorated
        animations = generalConfig.animations
        defaultTab = tabConfigMapper.toProto(generalConfig.defaultTab)
    }
}
package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.proto.Config
import app.vercors.launcher.core.config.proto.config
import org.koin.core.annotation.Single

@Single
class AppConfigMapper(
    private val generalConfigMapper: GeneralConfigMapper,
    private val homeConfigMapper: HomeConfigMapper
) {
    fun fromProto(config: Config): AppConfig = AppConfig(
        general = generalConfigMapper.fromProto(config.general),
        home = homeConfigMapper.fromProto(config.home)
    )

    fun toProto(appConfig: AppConfig): Config = config {
        general = generalConfigMapper.toProto(appConfig.general)
        home = homeConfigMapper.toProto(appConfig.home)
    }
}
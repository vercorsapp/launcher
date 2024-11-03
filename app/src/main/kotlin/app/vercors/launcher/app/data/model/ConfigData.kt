package app.vercors.launcher.app.data.model

import app.vercors.launcher.home.data.model.HomeConfigData
import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val app: AppConfigData,
    val home: HomeConfigData,
)

package app.vercors.launcher.app.data.model

import app.vercors.launcher.home.data.model.HomeConfigData
import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val general: GeneralConfigData,
    val home: HomeConfigData,
)

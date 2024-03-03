package app.vercors.root.setup

import app.vercors.configuration.ConfigurationData

data class SetupUiState(
    val config: ConfigurationData,
    val path: String,
    val showDirectoryPicker: Boolean = false,
    val showTutorial: Boolean = true
)

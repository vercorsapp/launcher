package app.vercors.configuration

data class ConfigurationUiState(
    val java8Status: ConfigurationJavaStatus = ConfigurationJavaStatus.Checking,
    val java17Status: ConfigurationJavaStatus = ConfigurationJavaStatus.Checking,
    val showDirectoryPicker: Boolean = false,
    val initialPath: String? = null,
    val onSelectPath: ConfigurationData.(String) -> ConfigurationData = { this },
    val hasCustomMemory: Boolean,
    val currentMemory: Int,
    val totalMemory: Int = 0
)

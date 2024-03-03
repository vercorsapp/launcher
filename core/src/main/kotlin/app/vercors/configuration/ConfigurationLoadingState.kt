package app.vercors.configuration

sealed interface ConfigurationLoadingState {
    data object NotLoaded : ConfigurationLoadingState
    data class Loaded(val config: ConfigurationData) : ConfigurationLoadingState
    data class Errored(val error: Throwable) : ConfigurationLoadingState
}
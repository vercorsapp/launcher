package com.skyecodes.vercors.settings

sealed interface ConfigurationState {
    data object NotLoaded : ConfigurationState
    data class Errored(val error: ConfigurationLoadingException) : ConfigurationState
    data class Loaded(val config: Configuration) : ConfigurationState
}
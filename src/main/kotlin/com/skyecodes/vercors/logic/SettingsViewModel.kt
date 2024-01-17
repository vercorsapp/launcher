package com.skyecodes.vercors.logic

import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.Provider
import moe.tlaster.precompose.viewmodel.ViewModel

class SettingsViewModel(
    private val onConfigurationChange: (Configuration) -> Unit
) : ViewModel() {
    fun onConfigChange(configuration: Configuration) {
        if (configuration.homeProviders.isEmpty()) return
        onConfigurationChange(configuration)
    }

    fun onHomeProviderChanged(it: Provider, configuration: Configuration) {
        val providers =
            if (it in configuration.homeProviders) configuration.homeProviders - it else configuration.homeProviders + it
        if (providers.isNotEmpty()) onConfigChange(configuration.copy(homeProviders = providers))
    }
}
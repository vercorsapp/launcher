package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.Provider
import com.skyecodes.vercors.data.service.ConfigurationService

interface SettingsComponent : Refreshable {
    fun onConfigChange(configuration: Configuration)
    fun onHomeProviderChanged(it: Provider, configuration: Configuration)

}

class DefaultSettingsComponent(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.get()
) : AbstractComponent(componentContext), SettingsComponent {
    override fun onConfigChange(configuration: Configuration) {
        if (configuration.homeProviders.isEmpty()) return
        configurationService.update(configuration)
    }

    override fun onHomeProviderChanged(it: Provider, configuration: Configuration) {
        val providers =
            if (it in configuration.homeProviders) configuration.homeProviders - it
            else configuration.homeProviders + it
        if (providers.isNotEmpty()) onConfigChange(configuration.copy(homeProviders = providers))
    }

    override fun refresh() {
        configurationService.load()
    }
}
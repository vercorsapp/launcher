package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.HomeSectionType
import com.skyecodes.vercors.data.model.app.Provider
import com.skyecodes.vercors.data.service.ConfigurationService

interface SettingsComponent : Refreshable {
    fun onConfigChange(configuration: Configuration)
    fun onHomeProviderChanged(provider: Provider, configuration: Configuration)
    fun onHomeSectionChanged(section: HomeSectionType, configuration: Configuration)

}

class DefaultSettingsComponent(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.get()
) : AbstractComponent(componentContext), SettingsComponent {
    override fun onConfigChange(configuration: Configuration) {
        if (configuration.homeProviders.isEmpty()) return
        configurationService.update(configuration)
    }

    override fun onHomeProviderChanged(provider: Provider, configuration: Configuration) {
        val providers =
            if (provider in configuration.homeProviders) configuration.homeProviders - provider
            else configuration.homeProviders + provider
        if (providers.isNotEmpty()) onConfigChange(configuration.copy(homeProviders = providers.sorted()))
    }

    override fun onHomeSectionChanged(section: HomeSectionType, configuration: Configuration) {
        val sections =
            if (section in configuration.homeSections) configuration.homeSections - section
            else configuration.homeSections + section
        if (sections.isNotEmpty()) onConfigChange(configuration.copy(homeSections = sections.sorted()))
    }

    override fun refresh() {
        configurationService.load()
    }
}
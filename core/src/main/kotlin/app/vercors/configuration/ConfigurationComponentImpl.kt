package app.vercors.configuration

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.home.HomeSectionType
import app.vercors.project.ProjectProviderType

class ConfigurationComponentImpl(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.inject()
) : AbstractAppComponent(componentContext), ConfigurationComponent {

    override fun onConfigChange(config: ConfigurationData) {
        configurationService.update(config)
    }

    override fun onHomeSectionChanged(section: HomeSectionType, configuration: ConfigurationData) {
        val sections =
            if (section in configuration.homeSections) configuration.homeSections - section
            else configuration.homeSections + section
        if (sections.isNotEmpty()) onConfigChange(configuration.copy(homeSections = sections.sorted()))
    }

    override fun onHomeProviderChanged(provider: ProjectProviderType, configuration: ConfigurationData) {
        val providers =
            if (provider in configuration.homeProviders) configuration.homeProviders - provider
            else configuration.homeProviders + provider
        if (providers.isNotEmpty()) onConfigChange(configuration.copy(homeProviders = providers.sorted()))
    }

    override fun refresh() {
        configurationService.load()
    }
}
package app.vercors.configuration

import app.vercors.common.Refreshable
import app.vercors.home.HomeSectionType
import app.vercors.navigation.NavigationChildComponent
import app.vercors.project.ProjectProviderType

interface ConfigurationComponent : NavigationChildComponent, Refreshable {
    fun onConfigChange(config: ConfigurationData)
    fun onHomeSectionChanged(section: HomeSectionType, configuration: ConfigurationData)
    fun onHomeProviderChanged(provider: ProjectProviderType, configuration: ConfigurationData)
}
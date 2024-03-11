package app.vercors.configuration

import app.vercors.common.Refreshable
import app.vercors.home.HomeSectionType
import app.vercors.navigation.NavigationChildComponent
import app.vercors.project.ProjectProviderType
import kotlinx.coroutines.flow.StateFlow

interface ConfigurationComponent : NavigationChildComponent, Refreshable {
    val uiState: StateFlow<ConfigurationUiState>
    fun onConfigChange(config: ConfigurationData)
    fun onHomeSectionChanged(section: HomeSectionType, configuration: ConfigurationData)
    fun onHomeProviderChanged(provider: ProjectProviderType, configuration: ConfigurationData)
    fun closeDirectoryPicker()
    fun openDirectoryPicker(initialPath: String?, onSelectPath: ConfigurationData.(String) -> ConfigurationData)
    fun toggleCustomMemory()
    fun onCustomMemoryChange(value: Int, fromSlider: Boolean = true)
}
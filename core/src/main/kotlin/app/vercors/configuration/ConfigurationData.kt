package app.vercors.configuration

import app.vercors.common.AppColor
import app.vercors.common.AppDarkTheme
import app.vercors.common.AppTab
import app.vercors.common.AppTheme
import app.vercors.home.HomeSectionType
import app.vercors.instance.InstanceSorter
import app.vercors.project.ProjectProviderType
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationData(
    val version: Int = 0,
    val showTutorial: Boolean = false,
    val theme: AppTheme = AppTheme.System,
    val darkTheme: AppDarkTheme = AppDarkTheme.Normal,
    val accentColor: AppColor = AppColor.Mauve,
    val useSystemWindowFrame: Boolean = false,
    val animations: Boolean = true,
    val defaultTab: AppTab = AppTab.Home,
    val homeSections: List<HomeSectionType> = HomeSectionType.entries,
    val homeProviders: List<ProjectProviderType> = ProjectProviderType.entries,
    val savedInstanceSorter: InstanceSorter = InstanceSorter(),
    val maximumParallelThreads: Int = Runtime.getRuntime().availableProcessors().coerceAtMost(8)
) {
    companion object {
        val DEFAULT = ConfigurationData()
    }
}
package app.vercors.navigation

import app.vercors.common.AppTab
import app.vercors.instance.InstanceData
import app.vercors.project.ProjectData
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class NavigationConfig(
    @Transient
    val tab: AppTab = AppTab.Home
) {
    @Serializable
    data object Home : NavigationConfig(AppTab.Home)

    @Serializable
    data object InstanceList : NavigationConfig(AppTab.Instances)

    @Serializable
    data object Search : NavigationConfig(AppTab.Search)

    @Serializable
    data object Configuration : NavigationConfig(AppTab.Settings)

    @Serializable
    data class InstanceDetails(val instance: InstanceData) : NavigationConfig(AppTab.Instances)

    @Serializable
    data class ProjectDetails(val project: ProjectData) : NavigationConfig(AppTab.Search)
}
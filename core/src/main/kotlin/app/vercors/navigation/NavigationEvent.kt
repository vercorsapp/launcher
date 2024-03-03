package app.vercors.navigation

import app.vercors.instance.InstanceData
import app.vercors.project.ProjectData

sealed interface NavigationEvent {
    data object Previous : NavigationEvent
    data object Next : NavigationEvent
    data object Refresh : NavigationEvent
    data object Home : NavigationEvent
    data object InstanceList : NavigationEvent
    data object Search : NavigationEvent
    data object Configuration : NavigationEvent
    data class InstanceDetails(val instance: InstanceData) : NavigationEvent
    data class ProjectDetails(val project: ProjectData) : NavigationEvent
}

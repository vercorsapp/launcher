package app.vercors.home

import app.vercors.common.Refreshable
import app.vercors.instance.InstanceData
import app.vercors.navigation.NavigationChildComponent
import app.vercors.project.ProjectData
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent : NavigationChildComponent, Refreshable {
    val uiState: StateFlow<HomeUiState>
    fun onShowInstanceDetails(instance: InstanceData)
    fun onLaunchInstance(instance: InstanceData)
    fun onShowProjectDetails(project: ProjectData)
    fun onInstallProject(project: ProjectData)
}
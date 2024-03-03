package app.vercors.home

import app.vercors.common.Refreshable
import app.vercors.instance.InstanceData
import app.vercors.navigation.NavigationChildComponent
import app.vercors.project.ProjectData
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent : NavigationChildComponent, Refreshable {
    val uiState: StateFlow<HomeUiState>
    val onShowInstanceDetails: (InstanceData) -> Unit
    val onLaunchInstance: (InstanceData) -> Unit
    val onShowProjectDetails: (ProjectData) -> Unit
    val onInstallProject: (ProjectData) -> Unit
}
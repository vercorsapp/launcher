package app.vercors.launcher.home.presentation.action

import app.vercors.launcher.instance.domain.model.InstanceId
import app.vercors.launcher.project.domain.model.ProjectId

sealed interface HomeAction {
    sealed interface Nav : HomeAction {
        data class ShowInstance(val instanceId: InstanceId) : Nav
        data class LaunchInstance(val instanceId: InstanceId) : Nav
        data class ShowProject(val projectId: ProjectId) : Nav
        data class InstallProject(val projectId: ProjectId) : Nav
    }
}
package app.vercors.launcher.home.presentation

import app.vercors.launcher.instance.domain.InstanceId
import app.vercors.launcher.project.domain.ProjectId

sealed interface HomeUiEffect {
    data object CreateInstance : HomeUiEffect

    @JvmInline
    value class NavigateToInstance(val instanceId: InstanceId) : HomeUiEffect

    @JvmInline
    value class NavigateToProject(val projectId: ProjectId) : HomeUiEffect
}

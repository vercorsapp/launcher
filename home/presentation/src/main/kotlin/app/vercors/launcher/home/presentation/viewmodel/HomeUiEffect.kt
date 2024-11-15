package app.vercors.launcher.home.presentation.viewmodel

import app.vercors.launcher.instance.domain.model.InstanceId
import app.vercors.launcher.project.domain.model.ProjectId

sealed interface HomeUiEffect {
    data object CreateInstance : HomeUiEffect

    @JvmInline
    value class NavigateToInstance(val instanceId: InstanceId) : HomeUiEffect

    @JvmInline
    value class NavigateToProject(val projectId: ProjectId) : HomeUiEffect
}

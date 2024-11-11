package app.vercors.launcher.home.presentation.viewmodel

import app.vercors.launcher.instance.domain.model.InstanceId
import app.vercors.launcher.project.domain.model.ProjectId

sealed interface HomeUiIntent : HomeUiEvent {
    @JvmInline
    value class ShowInstance(val instanceId: InstanceId) : HomeUiIntent

    @JvmInline
    value class LaunchOrStopInstance(val instanceId: InstanceId) : HomeUiIntent

    @JvmInline
    value class ShowProject(val projectId: ProjectId) : HomeUiIntent

    @JvmInline
    value class InstallProject(val projectId: ProjectId) : HomeUiIntent
    data object CreateInstance : HomeUiIntent
}
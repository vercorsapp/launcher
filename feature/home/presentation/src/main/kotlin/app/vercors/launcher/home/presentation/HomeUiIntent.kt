package app.vercors.launcher.home.presentation

import app.vercors.launcher.instance.domain.InstanceId
import app.vercors.launcher.project.domain.ProjectId

sealed interface HomeUiIntent {
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
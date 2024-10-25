package app.vercors.launcher.home.presentation.action

import kotlin.uuid.Uuid

sealed interface HomeAction {
    data class ShowInstance(val instanceId: Uuid) : HomeAction
    data class LaunchInstance(val instanceId: Uuid) : HomeAction
    data class ShowProject(val provider: String, val projectId: String) : HomeAction
    data class InstallProject(val provider: String, val projectId: String) : HomeAction
}
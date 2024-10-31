package app.vercors.launcher.app.navigation

import app.vercors.launcher.instance.domain.model.InstanceId
import app.vercors.launcher.project.domain.model.ProjectId
import kotlinx.serialization.Serializable

sealed interface AppDestination {
    @Serializable
    data object Home : AppDestination

    @Serializable
    data object Settings : AppDestination

    @Serializable
    data object Accounts : AppDestination

    @Serializable
    data object InstanceList : AppDestination

    @Serializable
    data class InstanceDetails(val instanceId: InstanceId) : AppDestination

    @Serializable
    data object ProjectList : AppDestination

    @Serializable
    data class ProjectDetails(val projectId: ProjectId) : AppDestination
}
package app.vercors.launcher.core.presentation.navigation

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

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
    data class InstanceDetails(@Contextual val instanceId: Uuid) : AppDestination

    @Serializable
    data object ProjectList : AppDestination

    @Serializable
    data class ProjectDetails(val provider: String, val projectId: String) : AppDestination
}
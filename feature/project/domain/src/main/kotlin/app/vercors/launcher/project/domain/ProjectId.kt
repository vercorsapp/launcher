package app.vercors.launcher.project.domain

import kotlinx.serialization.Serializable

@Serializable
data class ProjectId(
    val provider: ProjectProvider,
    val id: String
)

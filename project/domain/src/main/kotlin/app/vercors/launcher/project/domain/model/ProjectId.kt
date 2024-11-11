package app.vercors.launcher.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectId(
    val provider: ProjectProvider,
    val id: String
)

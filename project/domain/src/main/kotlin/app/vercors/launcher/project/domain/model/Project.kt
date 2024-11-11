package app.vercors.launcher.project.domain.model

import kotlinx.datetime.Instant

data class Project(
    val id: ProjectId,
    val name: String,
    val author: String,
    val description: String,
    val type: ProjectType,
    val iconUrl: String?,
    val bannerUrl: String?,
    val downloads: Long,
    val lastUpdated: Instant
)

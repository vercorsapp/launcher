package app.vercors.launcher.project.data.remote.modrinth

import app.vercors.launcher.project.data.remote.modrinth.dto.ModrinthProjectResult
import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectId
import app.vercors.launcher.project.domain.ProjectProvider

fun ModrinthProjectResult.toProject(): Project = Project(
    id = ProjectId(ProjectProvider.Modrinth, projectId),
    name = title,
    author = author,
    description = description,
    type = projectType.toProjectType(),
    iconUrl = iconUrl,
    bannerUrl = gallery?.firstOrNull(),
    downloads = downloads,
    lastUpdated = dateModified.toInstant()
)
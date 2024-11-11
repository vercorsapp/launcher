package app.vercors.launcher.project.data.remote.modrinth.mapper

import app.vercors.launcher.project.data.remote.modrinth.model.ModrinthProjectResult
import app.vercors.launcher.project.domain.model.Project
import app.vercors.launcher.project.domain.model.ProjectId
import app.vercors.launcher.project.domain.model.ProjectProvider

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
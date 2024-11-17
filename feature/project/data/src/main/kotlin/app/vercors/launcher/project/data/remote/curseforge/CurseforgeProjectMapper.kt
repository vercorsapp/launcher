package app.vercors.launcher.project.data.remote.curseforge

import app.vercors.launcher.project.data.remote.curseforge.dto.CurseforgeProject
import app.vercors.launcher.project.domain.Project
import app.vercors.launcher.project.domain.ProjectId
import app.vercors.launcher.project.domain.ProjectProvider

fun CurseforgeProject.toProject(): Project = Project(
    id = ProjectId(ProjectProvider.Curseforge, id.toString()),
    name = name,
    author = authors.first().name,
    description = summary,
    type = classId.toProjectType(),
    iconUrl = logo.thumbnailUrl,
    bannerUrl = screenshots.firstOrNull()?.thumbnailUrl,
    downloads = downloadCount,
    lastUpdated = dateModified.toInstant()
)
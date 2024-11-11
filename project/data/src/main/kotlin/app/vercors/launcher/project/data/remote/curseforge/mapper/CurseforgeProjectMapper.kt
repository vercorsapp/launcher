package app.vercors.launcher.project.data.remote.curseforge.mapper

import app.vercors.launcher.project.data.remote.curseforge.model.CurseforgeProject
import app.vercors.launcher.project.domain.model.Project
import app.vercors.launcher.project.domain.model.ProjectId
import app.vercors.launcher.project.domain.model.ProjectProvider

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
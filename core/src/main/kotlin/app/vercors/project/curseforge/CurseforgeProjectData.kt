package app.vercors.project.curseforge

import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import app.vercors.project.curseforge.data.CurseforgeProject

class CurseforgeProjectData(project: CurseforgeProject) : ProjectData {
    override val provider = ProjectProviderType.Curseforge
    override val id = project.id.toString()
    override val slug = project.slug
    override val name = project.name
    override val author = project.authors[0].name
    override val description = project.summary
    override val url = project.links.websiteUrl
    override val logoUrl = project.logo.thumbnailUrl
    override val imageUrl = project.screenshots.firstOrNull()?.thumbnailUrl
    override val downloads = project.downloadCount
    override val lastUpdated = project.dateModified
    //override val modLoaders = mod.latestFilesIndexes.mapNotNull { it.modLoader }.distinct().toModLoadersCurseforge()
}

fun List<CurseforgeProject>.convertAll(): List<ProjectData> = map { it.convert() }

fun CurseforgeProject.convert(): ProjectData = CurseforgeProjectData(this)
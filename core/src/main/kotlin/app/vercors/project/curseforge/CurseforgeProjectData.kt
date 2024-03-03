package app.vercors.project.curseforge

import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import app.vercors.project.curseforge.data.CurseforgeProject

class CurseforgeProjectData(mod: CurseforgeProject) : ProjectData {
    override val provider = ProjectProviderType.Curseforge
    override val id = mod.id.toString()
    override val slug = mod.slug
    override val name = mod.name
    override val author = mod.authors[0].name
    override val description = mod.summary
    override val url = mod.links.websiteUrl
    override val logoUrl = mod.logo.thumbnailUrl
    override val imageUrl = mod.screenshots.firstOrNull()?.thumbnailUrl
    override val downloads = mod.downloadCount
    override val lastUpdated = mod.dateModified
    //override val modLoaders = mod.latestFilesIndexes.mapNotNull { it.modLoader }.distinct().toModLoadersCurseforge()
}

fun List<CurseforgeProject>.convertAll(): List<ProjectData> = map { it.convert() }

fun CurseforgeProject.convert(): ProjectData = CurseforgeProjectData(this)
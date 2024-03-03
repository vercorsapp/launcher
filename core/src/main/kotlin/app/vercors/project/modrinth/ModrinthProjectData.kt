package app.vercors.project.modrinth

import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import app.vercors.project.modrinth.data.ModrinthProjectResult

class ModrinthProjectData(mod: ModrinthProjectResult) : ProjectData {
    override val provider = ProjectProviderType.Modrinth
    override val id = mod.projectId
    override val slug = mod.slug
    override val name = mod.title
    override val author = mod.author
    override val description = mod.description
    override val url = "https://modrinth.com/${mod.projectType.value}/${mod.slug}"
    override val logoUrl = mod.iconUrl
    override val imageUrl = mod.featuredGallery ?: mod.gallery?.firstOrNull()
    override val downloads = mod.downloads
    override val lastUpdated = mod.dateModified
    //override val modLoaders = mod.categories?.toModLoadersModrinth() ?: emptyList()
}

fun List<ModrinthProjectResult>.convertAll(): List<ProjectData> = map { it.convert() }

fun ModrinthProjectResult.convert(): ProjectData = ModrinthProjectData(this)
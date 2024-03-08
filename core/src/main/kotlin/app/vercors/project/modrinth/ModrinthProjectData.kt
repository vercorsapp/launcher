package app.vercors.project.modrinth

import app.vercors.project.ProjectData
import app.vercors.project.ProjectProviderType
import app.vercors.project.modrinth.data.ModrinthProjectResult

class ModrinthProjectData(project: ModrinthProjectResult) : ProjectData {
    override val provider = ProjectProviderType.Modrinth
    override val id = project.projectId
    override val slug = project.slug
    override val name = project.title
    override val author = project.author
    override val description = project.description
    override val url = "https://modrinth.com/${project.projectType.value}/${project.slug}"
    override val logoUrl = project.iconUrl
    override val imageUrl = project.featuredGallery ?: project.gallery?.firstOrNull()
    override val downloads = project.downloads
    override val lastUpdated = project.dateModified
    //override val modLoaders = mod.categories?.toModLoadersModrinth() ?: emptyList()
}

fun List<ModrinthProjectResult>.convertAll(): List<ProjectData> = map { it.convert() }

fun ModrinthProjectResult.convert(): ProjectData = ModrinthProjectData(this)
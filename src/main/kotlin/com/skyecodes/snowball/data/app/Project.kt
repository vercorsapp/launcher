package com.skyecodes.snowball.data.app

import com.skyecodes.snowball.data.curseforge.CurseforgeProject
import com.skyecodes.snowball.data.modrinth.ModrinthProjectResult
import java.time.Instant

interface Project {
    val provider: Provider
    val key: String
    val name: String
    val author: String
    val description: String
    val url: String
    val logoUrl: String?
    val imageUrl: String?
    val downloads: Long
    val lastUpdated: Instant
    //val modLoaders: List<ModLoader>
}

private class CurseforgeProjectAdapter(mod: CurseforgeProject) : Project {
    override val provider = Provider.Curseforge
    override val key = "curseforge-${mod.slug}"
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

private class ModrinthProjectAdapter(mod: ModrinthProjectResult) : Project {
    override val provider = Provider.Modrinth
    override val key = "modrinth-${mod.slug}"
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

fun List<CurseforgeProject>.convertCurseforge(): List<Project> = map { it.convert() }

fun CurseforgeProject.convert(): Project = CurseforgeProjectAdapter(this)

fun List<ModrinthProjectResult>.convertModrinth(): List<Project> = map { it.convert() }

fun ModrinthProjectResult.convert(): Project = ModrinthProjectAdapter(this)
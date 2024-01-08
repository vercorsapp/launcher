package com.skyecodes.snowball.data.app

import com.skyecodes.snowball.data.curseforge.CurseforgeProject
import com.skyecodes.snowball.data.modrinth.ModrinthProjectResult

interface Mod {
    val provider: Provider
    val key: String
    val name: String
    val description: String
    val url: String
    val logoUrl: String?
    val modLoaders: List<ModLoader>
}

private class CurseforgeModAdapter(mod: CurseforgeProject) : Mod {
    override val provider = Provider.Curseforge
    override val key = "curseforge-${mod.slug}"
    override val name = mod.name
    override val description = mod.summary
    override val url = mod.links.websiteUrl
    override val logoUrl = mod.logo.thumbnailUrl
    override val modLoaders = mod.latestFilesIndexes.mapNotNull { it.modLoader }.distinct().toModLoadersCurseforge()
}

private class ModrinthModAdapter(mod: ModrinthProjectResult) : Mod {
    override val provider = Provider.Modrinth
    override val key = "modrinth-${mod.slug}"
    override val name = mod.title
    override val description = mod.description
    override val url = "https://modrinth.com/${mod.projectType.value}/${mod.slug}"
    override val logoUrl = mod.iconUrl
    override val modLoaders = mod.categories?.toModLoadersModrinth() ?: emptyList()
}

fun List<CurseforgeProject>.convertCurseforge(): List<Mod> = map { it.convert() }

fun CurseforgeProject.convert(): Mod = CurseforgeModAdapter(this)

fun List<ModrinthProjectResult>.convertModrinth(): List<Mod> = map { it.convert() }

fun ModrinthProjectResult.convert(): Mod = ModrinthModAdapter(this)
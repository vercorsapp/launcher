package com.skyecodes.snowball.data.app

import com.skyecodes.snowball.data.curseforge.CurseforgeMod
import com.skyecodes.snowball.data.modrinth.ModrinthProjectResult

interface Mod {
    val provider: Provider
    val name: String
    val description: String
    val url: String
    val logoUrl: String?
}

private class CurseforgeModAdapter(mod: CurseforgeMod) : Mod {
    override val provider = Provider.Curseforge
    override val name = mod.name
    override val description = mod.summary
    override val url = mod.links.websiteUrl
    override val logoUrl = mod.logo.thumbnailUrl
}

private class ModrinthModAdapter(mod: ModrinthProjectResult) : Mod {
    override val provider = Provider.Modrinth
    override val name = mod.title
    override val description = mod.description
    override val url = "https://modrinth.com/${mod.projectType.value}/${mod.slug}"
    override val logoUrl = mod.iconUrl
}

fun List<CurseforgeMod>.fromCurseforge(): List<Mod> = map { it.fromCurseforge() }

fun CurseforgeMod.fromCurseforge(): Mod = CurseforgeModAdapter(this)

fun List<ModrinthProjectResult>.fromModrinth(): List<Mod> = map { it.fromModrinth() }

fun ModrinthProjectResult.fromModrinth(): Mod = ModrinthModAdapter(this)
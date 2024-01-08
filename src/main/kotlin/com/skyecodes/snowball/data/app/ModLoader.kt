package com.skyecodes.snowball.data.app

import com.skyecodes.snowball.data.curseforge.CurseforgeModLoaderType

enum class ModLoader(val strName: String) {
    Forge("forge"),
    NeoForge("neoforge"),
    Fabric("fabric"),
    Quilt("quilt");

    val iconPath: String = "/icon/svg/$strName.svg"
}

fun List<String>.toModLoadersModrinth() = mapNotNull { it.toModLoader() }

fun String.toModLoader(): ModLoader? = ModLoader.entries.firstOrNull { it.strName == this }

fun List<CurseforgeModLoaderType>.toModLoadersCurseforge() = mapNotNull { it.toModLoader() }

fun CurseforgeModLoaderType.toModLoader(): ModLoader? = when (this) {
    CurseforgeModLoaderType.Forge -> ModLoader.Forge
    CurseforgeModLoaderType.NeoForge -> ModLoader.NeoForge
    CurseforgeModLoaderType.Fabric -> ModLoader.Fabric
    CurseforgeModLoaderType.Quilt -> ModLoader.Quilt
    else -> null
}

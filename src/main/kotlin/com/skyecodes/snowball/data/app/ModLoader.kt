package com.skyecodes.snowball.data.app

import com.skyecodes.snowball.data.StringEnumerable
import com.skyecodes.snowball.data.StringEnumerableSerializer
import com.skyecodes.snowball.data.curseforge.CurseforgeModLoaderType
import kotlinx.serialization.Serializable

@Serializable(with = ModLoaderSerializer::class)
enum class ModLoader(override val value: String) : StringEnumerable {
    Forge("forge"),
    NeoForge("neoforge"),
    Fabric("fabric"),
    Quilt("quilt");

    val iconPath: String = "/icon/svg/$value.svg"
}

private class ModLoaderSerializer : StringEnumerableSerializer<ModLoader>(ModLoader.entries)

fun List<String>.toModLoadersModrinth() = mapNotNull { it.toModLoader() }

fun String.toModLoader(): ModLoader? = ModLoader.entries.firstOrNull { it.value == this }

fun List<CurseforgeModLoaderType>.toModLoadersCurseforge() = mapNotNull { it.toModLoader() }

fun CurseforgeModLoaderType.toModLoader(): ModLoader? = when (this) {
    CurseforgeModLoaderType.Forge -> ModLoader.Forge
    CurseforgeModLoaderType.NeoForge -> ModLoader.NeoForge
    CurseforgeModLoaderType.Fabric -> ModLoader.Fabric
    CurseforgeModLoaderType.Quilt -> ModLoader.Quilt
    else -> null
}

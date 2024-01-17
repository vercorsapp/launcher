package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import com.skyecodes.vercors.data.model.curseforge.CurseforgeModLoaderType
import kotlinx.serialization.Serializable

@Serializable(LoaderSerializer::class)
enum class Loader(override val value: String) : StringEnumerable {
    Forge("forge"),
    NeoForge("neoforge"),
    Fabric("fabric"),
    Quilt("quilt");
}

private class LoaderSerializer : StringEnumerableSerializer<Loader>(Loader.entries)

fun List<String>.toLoadersModrinth() = mapNotNull { it.toLoader() }

fun String.toLoader(): Loader? = Loader.entries.firstOrNull { it.value == this }

fun List<CurseforgeModLoaderType>.toLoadersCurseforge() = mapNotNull { it.toLoader() }

fun CurseforgeModLoaderType.toLoader(): Loader? = when (this) {
    CurseforgeModLoaderType.Forge -> Loader.Forge
    CurseforgeModLoaderType.NeoForge -> Loader.NeoForge
    CurseforgeModLoaderType.Fabric -> Loader.Fabric
    CurseforgeModLoaderType.Quilt -> Loader.Quilt
    else -> null
}

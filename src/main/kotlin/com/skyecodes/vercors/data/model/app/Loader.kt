package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.curseforge.CurseforgeModLoaderType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Loader(val value: String) {
    @SerialName("forge")
    Forge("forge"),

    @SerialName("neoforge")
    NeoForge("neoforge"),

    @SerialName("fabric")
    Fabric("fabric"),

    @SerialName("quilt")
    Quilt("quilt")
}

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

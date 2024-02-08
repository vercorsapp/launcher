package com.skyecodes.vercors.projects.curseforge

import com.skyecodes.vercors.common.IntEnumerable
import com.skyecodes.vercors.common.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(CurseforgeModLoaderTypeSerializer::class)
enum class CurseforgeModLoaderType(override val value: Int) : IntEnumerable {
    Any(0),
    Forge(1),
    Cauldron(2),
    LiteLoader(3),
    Fabric(4),
    Quilt(5),
    NeoForge(6)
}

private class CurseforgeModLoaderTypeSerializer :
    IntEnumerableSerializer<CurseforgeModLoaderType>(CurseforgeModLoaderType.entries)
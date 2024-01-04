package com.skyecodes.snowball.data.curseforge

import com.skyecodes.snowball.data.IntEnumerable
import com.skyecodes.snowball.data.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CurseforgeFileRelationTypeSerializer::class)
enum class CurseforgeFileRelationType(override val value: Int) : IntEnumerable {
    EmbeddedLibrary(1),
    OptionalDependency(2),
    RequiredDependency(3),
    Tool(4),
    Incompatible(5),
    Include(6)
}

private class CurseforgeFileRelationTypeSerializer :
    IntEnumerableSerializer<CurseforgeFileRelationType>(CurseforgeFileRelationType.entries)
package app.vercors.project.curseforge.data

import app.vercors.common.IntEnumerable
import app.vercors.common.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(CurseforgeFileRelationTypeSerializer::class)
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
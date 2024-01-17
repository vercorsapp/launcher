package com.skyecodes.vercors.data.model.curseforge

import com.skyecodes.vercors.data.model.IntEnumerable
import com.skyecodes.vercors.data.model.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(CurseforgeFileReleaseTypeSerializer::class)
enum class CurseforgeFileReleaseType(override val value: Int) : IntEnumerable {
    Release(1),
    Beta(2),
    Alpha(3)
}

private class CurseforgeFileReleaseTypeSerializer :
    IntEnumerableSerializer<CurseforgeFileReleaseType>(CurseforgeFileReleaseType.entries)
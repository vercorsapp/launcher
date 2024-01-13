package com.skyecodes.vercors.data.curseforge

import com.skyecodes.vercors.data.IntEnumerable
import com.skyecodes.vercors.data.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CurseforgeFileReleaseTypeSerializer::class)
enum class CurseforgeFileReleaseType(override val value: Int) : IntEnumerable {
    Release(1),
    Beta(2),
    Alpha(3)
}

private class CurseforgeFileReleaseTypeSerializer :
    IntEnumerableSerializer<CurseforgeFileReleaseType>(CurseforgeFileReleaseType.entries)
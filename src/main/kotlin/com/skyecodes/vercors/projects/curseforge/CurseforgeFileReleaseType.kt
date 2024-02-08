package com.skyecodes.vercors.projects.curseforge

import com.skyecodes.vercors.common.IntEnumerable
import com.skyecodes.vercors.common.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(CurseforgeFileReleaseTypeSerializer::class)
enum class CurseforgeFileReleaseType(override val value: Int) : IntEnumerable {
    Release(1),
    Beta(2),
    Alpha(3)
}

private class CurseforgeFileReleaseTypeSerializer :
    IntEnumerableSerializer<CurseforgeFileReleaseType>(CurseforgeFileReleaseType.entries)
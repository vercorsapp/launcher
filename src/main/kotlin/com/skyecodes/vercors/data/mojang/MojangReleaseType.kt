package com.skyecodes.vercors.data.mojang

import com.skyecodes.vercors.data.StringEnumerable
import com.skyecodes.vercors.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(MojangReleaseTypeSerializable::class)
enum class MojangReleaseType(override val value: String) : StringEnumerable {
    Release("release"),
    Snapshot("snapshot"),
    Beta("old_beta"),
    Alpha("old_alpha"),
}

private class MojangReleaseTypeSerializable : StringEnumerableSerializer<MojangReleaseType>(MojangReleaseType.entries)

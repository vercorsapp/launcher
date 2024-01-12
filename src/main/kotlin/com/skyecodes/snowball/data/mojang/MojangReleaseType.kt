package com.skyecodes.snowball.data.mojang

import com.skyecodes.snowball.data.StringEnumerable
import com.skyecodes.snowball.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(MojangReleaseTypeSerializable::class)
enum class MojangReleaseType(override val value: String) : StringEnumerable {
    Release("release"),
    Snapshot("snapshot"),
    Beta("old_beta"),
    Alpha("old_alpha"),
}

private class MojangReleaseTypeSerializable : StringEnumerableSerializer<MojangReleaseType>(MojangReleaseType.entries)

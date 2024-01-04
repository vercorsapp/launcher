package com.skyecodes.snowball.data.curseforge

import com.skyecodes.snowball.data.IntEnumerable
import com.skyecodes.snowball.data.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CurseforgeHashAlgoSerializer::class)
enum class CurseforgeHashAlgo(override val value: Int) : IntEnumerable {
    Sha1(1),
    Md5(2)
}

private class CurseforgeHashAlgoSerializer : IntEnumerableSerializer<CurseforgeHashAlgo>(CurseforgeHashAlgo.entries)
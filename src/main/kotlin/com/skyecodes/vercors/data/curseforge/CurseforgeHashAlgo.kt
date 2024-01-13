package com.skyecodes.vercors.data.curseforge

import com.skyecodes.vercors.data.IntEnumerable
import com.skyecodes.vercors.data.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CurseforgeHashAlgoSerializer::class)
enum class CurseforgeHashAlgo(override val value: Int) : IntEnumerable {
    Sha1(1),
    Md5(2)
}

private class CurseforgeHashAlgoSerializer : IntEnumerableSerializer<CurseforgeHashAlgo>(CurseforgeHashAlgo.entries)
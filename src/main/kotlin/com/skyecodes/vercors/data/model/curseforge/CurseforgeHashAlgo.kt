package com.skyecodes.vercors.data.model.curseforge

import com.skyecodes.vercors.data.model.IntEnumerable
import com.skyecodes.vercors.data.model.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(CurseforgeHashAlgoSerializer::class)
enum class CurseforgeHashAlgo(override val value: Int) : IntEnumerable {
    Sha1(1),
    Md5(2)
}

private class CurseforgeHashAlgoSerializer : IntEnumerableSerializer<CurseforgeHashAlgo>(CurseforgeHashAlgo.entries)
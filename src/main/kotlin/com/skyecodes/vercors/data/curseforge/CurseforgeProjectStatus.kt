package com.skyecodes.vercors.data.curseforge

import com.skyecodes.vercors.data.IntEnumerable
import com.skyecodes.vercors.data.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CurseforgeModStatusSerializer::class)
enum class CurseforgeProjectStatus(override val value: Int) : IntEnumerable {
    New(1),
    ChangesRequired(2),
    UnderSoftReview(3),
    Approved(4),
    Rejected(5),
    ChangesMade(6),
    Inactive(7),
    Abandoned(8),
    Deleted(9),
    UnderReview(10)
}

private class CurseforgeModStatusSerializer :
    IntEnumerableSerializer<CurseforgeProjectStatus>(CurseforgeProjectStatus.entries)
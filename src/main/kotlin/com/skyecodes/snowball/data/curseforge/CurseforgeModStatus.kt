package com.skyecodes.snowball.data.curseforge

import com.skyecodes.snowball.data.IntEnumerable
import com.skyecodes.snowball.data.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CurseforgeModStatusSerializer::class)
enum class CurseforgeModStatus(override val value: Int) : IntEnumerable {
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

private class CurseforgeModStatusSerializer : IntEnumerableSerializer<CurseforgeModStatus>(CurseforgeModStatus.entries)
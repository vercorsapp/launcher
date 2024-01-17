package com.skyecodes.vercors.data.model.curseforge

import com.skyecodes.vercors.data.model.IntEnumerable
import com.skyecodes.vercors.data.model.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(CurseforgeFileStatusSerializer::class)
enum class CurseforgeFileStatus(override val value: Int) : IntEnumerable {
    Processing(1),
    ChangesRequired(2),
    UnderReview(3),
    Approved(4),
    Rejected(5),
    MalwareDetected(6),
    Deleted(7),
    Archived(8),
    Testing(9),
    Released(10),
    ReadyForReview(11),
    Deprecated(12),
    Baking(13),
    AwaitingPublishing(14),
    FailedPublishing(15)
}

private class CurseforgeFileStatusSerializer :
    IntEnumerableSerializer<CurseforgeFileStatus>(CurseforgeFileStatus.entries)
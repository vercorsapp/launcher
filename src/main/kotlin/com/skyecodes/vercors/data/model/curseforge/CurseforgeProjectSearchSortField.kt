package com.skyecodes.vercors.data.model.curseforge

import com.skyecodes.vercors.data.model.IntEnumerable
import com.skyecodes.vercors.data.model.IntEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(CurseforgeProjectSearchSortFieldSerializer::class)
enum class CurseforgeProjectSearchSortField(override val value: Int) : IntEnumerable {
    Featured(1),
    Popularity(2),
    LastUpdated(3),
    Name(4),
    Author(5),
    TotalDownloads(6),
    Category(7),
    GameVersion(8),
    EarlyAccess(9),
    FeaturedReleased(10),
    ReleasedDate(11),
    Rating(12)
}

private class CurseforgeProjectSearchSortFieldSerializer :
    IntEnumerableSerializer<CurseforgeProjectSearchSortField>(CurseforgeProjectSearchSortField.entries)
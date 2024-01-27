package com.skyecodes.vercors.data.model.app

import kotlinx.serialization.Serializable

@Serializable
data class InstanceSorter(
    val sortBy: InstanceSortBy = InstanceSortBy.LastPlayed,
    val sortByOrder: SortOrder = SortOrder.Desc,
    val groupBy: InstanceGroupBy = InstanceGroupBy.None,
    val groupByOrder: SortOrder = SortOrder.Desc
)
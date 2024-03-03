package app.vercors.instance

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class InstanceListUiState(
    val instanceList: ImmutableList<InstanceData> = persistentListOf(),
    val instanceGroups: ImmutableMap<String, List<InstanceData>> = persistentMapOf(),
    val nameFilter: String = "",
    val sorter: InstanceSorter,
    val canSaveFilters: Boolean = false
)
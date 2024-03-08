package app.vercors.instance

data class InstanceListUiState(
    val isLoading: Boolean = true,
    val instanceList: List<InstanceData> = emptyList(),
    val instanceGroups: Map<String, List<InstanceData>> = emptyMap(),
    val nameFilter: String = "",
    val sorter: InstanceSorter,
    val canSaveFilters: Boolean = false
)
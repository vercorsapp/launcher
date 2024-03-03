package app.vercors.instance

import app.vercors.applyIf
import app.vercors.common.*
import app.vercors.configuration.ConfigurationService
import app.vercors.project.ModLoader
import app.vercors.toolbar.ToolbarTitle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class InstanceListComponentImpl(
    componentContext: AppComponentContext,
    override val onShowInstanceDetails: (InstanceData) -> Unit,
    override val onLaunchInstance: (InstanceData) -> Unit,
    override val onOpenCreateInstanceDialog: () -> Unit,
    private val configurationService: ConfigurationService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject()
) : AbstractAppComponent(componentContext), InstanceListComponent {
    override val tab = AppTab.Instances
    override val isDefault = true
    override val title = ToolbarTitle.Instances

    private val _uiState =
        MutableStateFlow(InstanceListUiState(sorter = configurationService.config.savedInstanceSorter))
    override val uiState: StateFlow<InstanceListUiState> = _uiState
    private lateinit var job: Job

    override fun onStart() {
        super.onStart()
        job = scope.launch {
            instanceService.instancesState.filterNotNull().collect { updateInstances(instances = it) }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    private fun updateInstances(
        instances: List<InstanceData> = instanceService.instances,
        nameFilter: String = uiState.value.nameFilter,
        sortBy: InstanceSortBy = uiState.value.sorter.sortBy,
        sortByOrder: SortOrder = uiState.value.sorter.sortByOrder,
        groupBy: InstanceGroupBy = uiState.value.sorter.groupBy,
        groupByOrder: SortOrder = uiState.value.sorter.groupByOrder,
        canSaveFilters: Boolean = uiState.value.canSaveFilters
    ) {
        val filter = nameFilter.lowercase().trim()
        val filteredInstances = instances.filter {
            filter.isBlank()
                    || filter in it.name.lowercase()
                    || filter in it.gameVersion.id.lowercase()
                    || filter in (it.loader?.value ?: ModLoader.Vanilla).lowercase()
        }
        val sortedInstances = filteredInstances.sortedWith(
            sortBy.comparator.applyIf(sortByOrder == SortOrder.Desc) { reversed() }
        )
        val groupedInstances = sortedInstances.applyGroupBy(groupBy.behavior, groupByOrder)
        _uiState.update {
            InstanceListUiState(
                instanceList = instances.toImmutableList(),
                instanceGroups = groupedInstances.toImmutableMap(),
                nameFilter = nameFilter,
                sorter = InstanceSorter(sortBy, sortByOrder, groupBy, groupByOrder),
                canSaveFilters = canSaveFilters
            )
        }
    }

    private fun <T> List<InstanceData>.applyGroupBy(
        groupBy: InstanceGroupBy.Behavior<T>,
        groupByOrder: SortOrder
    ): Map<String, List<InstanceData>> {
        val grouped = groupBy(groupBy.key)
        val groupedSorted =
            TreeMap<T, List<InstanceData>>(groupBy.comparator.applyIf(groupByOrder == SortOrder.Desc) { reversed() })
        groupedSorted.putAll(grouped)
        val result = LinkedHashMap<String, List<InstanceData>>()
        groupedSorted.forEach { (k, v) -> result[groupBy.header(k)] = v }
        return result
    }

    override fun updateNameFilter(nameFilter: String) {
        updateInstances(nameFilter = nameFilter)
    }

    override fun updateSortBy(sortBy: InstanceSortBy) {
        updateInstances(sortBy = sortBy, canSaveFilters = true)
    }

    override fun toggleSortByOrder() {
        updateInstances(sortByOrder = uiState.value.sorter.sortByOrder.opposite, canSaveFilters = true)
    }

    override fun updateGroupBy(groupBy: InstanceGroupBy) {
        updateInstances(groupBy = groupBy, canSaveFilters = true)
    }

    override fun toggleGroupByOrder() {
        updateInstances(groupByOrder = uiState.value.sorter.groupByOrder.opposite, canSaveFilters = true)
    }

    override fun saveFilters() {
        configurationService.update(configurationService.config.copy(savedInstanceSorter = uiState.value.sorter))
        _uiState.update { it.copy(canSaveFilters = false) }
    }

    override fun refresh() {
        instanceService.loadInstances()
    }
}
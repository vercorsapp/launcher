package app.vercors.instance

import app.vercors.applyIf
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.SortOrder
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationService
import app.vercors.dialog.DialogEvent
import app.vercors.dialog.DialogService
import app.vercors.instance.launch.LauncherService
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationService
import app.vercors.notification.NotificationService
import app.vercors.project.ModLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class InstanceListComponentImpl(
    componentContext: AppComponentContext,
    private val configurationService: ConfigurationService = componentContext.inject(),
    private val instanceService: InstanceService = componentContext.inject(),
    private val navigationService: NavigationService = componentContext.inject(),
    private val launcherService: LauncherService = componentContext.inject(),
    private val dialogService: DialogService = componentContext.inject(),
    private val notificationService: NotificationService = componentContext.inject()
) : AbstractAppComponent(componentContext), InstanceListComponent {

    private val _uiState =
        MutableStateFlow(InstanceListUiState(sorter = configurationService.config.savedInstanceSorter))
    override val uiState: StateFlow<InstanceListUiState> = _uiState

    init {
        instanceService.loadingState.collectInLifecycle {
            when (it) {
                is InstanceLoadingState.Loading -> updateState(isLoading = true, instances = it.instances)
                is InstanceLoadingState.Loaded -> updateState(isLoading = false, instances = it.instances)
            }
        }
    }

    private fun updateState(
        isLoading: Boolean = uiState.value.isLoading,
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
                isLoading = isLoading,
                instanceList = instances,
                instanceGroups = groupedInstances,
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

    override fun showInstanceDetails(instance: InstanceData) {
        navigationService.handle(NavigationEvent.InstanceDetails(instance))
    }

    override fun launchInstance(instance: InstanceData) {
        launch { launcherService.launch(instance).collect() }
    }

    override fun openCreateInstanceDialog() {
        dialogService.openDialog(DialogEvent.CreateInstance)
    }

    override fun updateNameFilter(nameFilter: String) {
        updateState(nameFilter = nameFilter)
    }

    override fun updateSortBy(sortBy: InstanceSortBy) {
        updateState(sortBy = sortBy, canSaveFilters = true)
    }

    override fun toggleSortByOrder() {
        updateState(sortByOrder = uiState.value.sorter.sortByOrder.opposite, canSaveFilters = true)
    }

    override fun updateGroupBy(groupBy: InstanceGroupBy) {
        updateState(groupBy = groupBy, canSaveFilters = true)
    }

    override fun toggleGroupByOrder() {
        updateState(groupByOrder = uiState.value.sorter.groupByOrder.opposite, canSaveFilters = true)
    }

    override fun saveFilters() {
        configurationService.update(configurationService.config.copy(savedInstanceSorter = uiState.value.sorter))
        _uiState.update { it.copy(canSaveFilters = false) }
    }

    override fun refresh() {
        instanceService.loadInstances()
    }
}
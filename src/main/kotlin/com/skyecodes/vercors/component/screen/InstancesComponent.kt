package com.skyecodes.vercors.component.screen

import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.skyecodes.vercors.applyIf
import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.*
import com.skyecodes.vercors.data.service.ConfigurationService
import com.skyecodes.vercors.data.service.InstanceService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

private val logger = KotlinLogging.logger { }

interface InstancesComponent : Refreshable {
    val uiState: StateFlow<UiState>
    val instances: StateFlow<List<Instance>>
    val openNewInstanceDialog: () -> Unit
    val showInstanceDetails: (Instance) -> Unit
    val launchInstance: (Instance) -> Unit

    fun updateNameFilter(nameFilter: String)
    fun updateSortBy(sortBy: InstanceSortBy)
    fun toggleSortByOrder()
    fun updateGroupBy(groupBy: InstanceGroupBy)
    fun toggleGroupByOrder()
    fun saveFilters()

    data class UiState(
        val nameFilter: String = "",
        val sorter: InstanceSorter,
        val canSaveFilters: Boolean = false,
        val instances: Map<String, List<Instance>> = linkedMapOf()
    )
}

class DefaultInstancesComponent(
    componentContext: AppComponentContext,
    override val openNewInstanceDialog: () -> Unit,
    override val showInstanceDetails: (Instance) -> Unit,
    override val launchInstance: (Instance) -> Unit,
    override val instances: StateFlow<List<Instance>>,
    private val configuration: StateFlow<Configuration>,
    private val instanceService: InstanceService = componentContext.get(),
    private val configurationService: ConfigurationService = componentContext.get()
) : AbstractComponent(componentContext), InstancesComponent {
    override val uiState =
        MutableStateFlow(InstancesComponent.UiState(sorter = configuration.value.savedInstanceSorter))
    private lateinit var instancesJob: Job

    init {
        doOnStart { onStart() }
        doOnStop { onStop() }
    }

    private fun onStart() {
        instancesJob = scope.launch { instances.collect { updateInstances() } }
    }

    private fun onStop() {
        instancesJob.cancel()
    }

    private fun updateInstances(
        nameFilter: String = uiState.value.nameFilter,
        sortBy: InstanceSortBy = uiState.value.sorter.sortBy,
        sortByOrder: SortOrder = uiState.value.sorter.sortByOrder,
        groupBy: InstanceGroupBy = uiState.value.sorter.groupBy,
        groupByOrder: SortOrder = uiState.value.sorter.groupByOrder,
        canSaveFilters: Boolean = uiState.value.canSaveFilters
    ) {
        val filter = nameFilter.lowercase().trim()
        val filteredInstances = instances.value.filter {
            filter.isBlank()
                    || filter in it.name.lowercase()
                    || filter in it.gameVersion.id.lowercase()
                    || filter in (it.loader?.value ?: Loader.Vanilla).lowercase()
        }
        val sortedInstances = filteredInstances.sortedWith(
            sortBy.comparator.applyIf(sortByOrder == SortOrder.Desc) { reversed() }
        )
        val groupedInstances = sortedInstances.applyGroupBy(groupBy.behavior, groupByOrder)
        uiState.value = InstancesComponent.UiState(
            nameFilter = nameFilter,
            sorter = InstanceSorter(sortBy, sortByOrder, groupBy, groupByOrder),
            canSaveFilters = canSaveFilters,
            instances = groupedInstances
        )
    }

    private fun <T> List<Instance>.applyGroupBy(
        groupBy: InstanceGroupBy.Behavior<T>,
        groupByOrder: SortOrder
    ): Map<String, List<Instance>> {
        val grouped = groupBy(groupBy.key)
        val groupedSorted =
            TreeMap<T, List<Instance>>(groupBy.comparator.applyIf(groupByOrder == SortOrder.Desc) { reversed() })
        groupedSorted.putAll(grouped)
        val result = LinkedHashMap<String, List<Instance>>()
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
        configurationService.updateConfiguration(configuration.value.copy(savedInstanceSorter = uiState.value.sorter))
        uiState.update { it.copy(canSaveFilters = false) }
    }

    override fun refresh() {
        instanceService.reloadInstances()
    }
}


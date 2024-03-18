/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.instance

import app.vercors.applyIf
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.SortOrder
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationService
import app.vercors.dialog.DialogConfig
import app.vercors.dialog.DialogService
import app.vercors.instance.launch.LauncherService
import app.vercors.navigation.NavigationEvent
import app.vercors.navigation.NavigationService
import app.vercors.notification.NotificationService
import app.vercors.project.ModLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    private val _state =
        MutableStateFlow(InstanceListState(sorter = configurationService.config.savedInstanceSorter))
    override val state: StateFlow<InstanceListState> = _state

    init {
        instanceService.loadingState.collectInLifecycle {
            when (it) {
                is InstanceLoadingState.Loading -> updateState(isLoading = true, instances = it.instances)
                is InstanceLoadingState.Loaded -> updateState(isLoading = false, instances = it.instances)
            }
        }
    }

    override fun onIntent(intent: InstanceListIntent) = when (intent) {
        InstanceListIntent.OpenCreateInstanceDialog -> onOpenCreateInstanceDialog()
        is InstanceListIntent.UpdateNameFilter -> onUpdateNameFilter(intent.nameFilter)
        is InstanceListIntent.UpdateSortBy -> onUpdateSortBy(intent.sortBy)
        InstanceListIntent.ToggleSortByOrder -> onToggleSortByOrder()
        is InstanceListIntent.UpdateGroupBy -> onUpdateGroupBy(intent.groupBy)
        InstanceListIntent.ToggleGroupByOrder -> onToggleGroupByOrder()
        InstanceListIntent.SaveFilters -> onSaveFilters()
        is InstanceListIntent.ShowInstanceDetails -> onShowInstanceDetails(intent.instance)
        is InstanceListIntent.LaunchInstance -> onLaunchInstance(intent.instance)
    }

    override fun refresh() {
        instanceService.loadInstances()
    }

    private fun updateState(
        isLoading: Boolean = state.value.isLoading,
        instances: List<InstanceData> = instanceService.instances,
        nameFilter: String = state.value.nameFilter,
        sortBy: InstanceSortBy = state.value.sorter.sortBy,
        sortByOrder: SortOrder = state.value.sorter.sortByOrder,
        groupBy: InstanceGroupBy = state.value.sorter.groupBy,
        groupByOrder: SortOrder = state.value.sorter.groupByOrder,
        canSaveFilters: Boolean = state.value.canSaveFilters
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
        _state.update {
            InstanceListState(
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

    private fun onShowInstanceDetails(instance: InstanceData) {
        navigationService.handle(NavigationEvent.InstanceDetails(instance))
    }

    private fun onLaunchInstance(instance: InstanceData) {
        launcherService.launchInstance(instance)
    }

    private fun onOpenCreateInstanceDialog() {
        dialogService.onOpenDialog(DialogConfig.CreateInstance)
    }

    private fun onUpdateNameFilter(nameFilter: String) {
        updateState(nameFilter = nameFilter)
    }

    private fun onUpdateSortBy(sortBy: InstanceSortBy) {
        updateState(sortBy = sortBy, canSaveFilters = true)
    }

    private fun onToggleSortByOrder() {
        updateState(sortByOrder = state.value.sorter.sortByOrder.opposite, canSaveFilters = true)
    }

    private fun onUpdateGroupBy(groupBy: InstanceGroupBy) {
        updateState(groupBy = groupBy, canSaveFilters = true)
    }

    private fun onToggleGroupByOrder() {
        updateState(groupByOrder = state.value.sorter.groupByOrder.opposite, canSaveFilters = true)
    }

    private fun onSaveFilters() {
        configurationService.update(false) { it.copy(savedInstanceSorter = state.value.sorter) }
        _state.update { it.copy(canSaveFilters = false) }
    }
}
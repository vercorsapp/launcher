package app.vercors.instance

import app.vercors.common.Refreshable
import app.vercors.navigation.NavigationChildComponent
import kotlinx.coroutines.flow.StateFlow

interface InstanceListComponent : NavigationChildComponent, Refreshable {
    val uiState: StateFlow<InstanceListUiState>

    fun showInstanceDetails(instance: InstanceData)
    fun launchInstance(instance: InstanceData)
    fun openCreateInstanceDialog()
    fun updateNameFilter(nameFilter: String)
    fun updateSortBy(sortBy: InstanceSortBy)
    fun toggleSortByOrder()
    fun updateGroupBy(groupBy: InstanceGroupBy)
    fun toggleGroupByOrder()
    fun saveFilters()
}
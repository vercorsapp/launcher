package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
import com.skyecodes.vercors.data.model.app.Instance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface InstancesComponent : Refreshable {
    val uiState: StateFlow<UiState>
    val instances: StateFlow<List<Instance>?>
    val openNewInstanceDialog: () -> Unit

    fun updateNameFilter(nameFilter: String)

    data class UiState(
        val nameFilter: String = ""
    )
}

class DefaultInstancesComponent(
    override val instances: StateFlow<List<Instance>?>,
    override val openNewInstanceDialog: () -> Unit,
    componentContext: AppComponentContext
) : AppComponentContext by componentContext, InstancesComponent {
    override val uiState = MutableStateFlow(InstancesComponent.UiState())

    override fun updateNameFilter(nameFilter: String) {
        uiState.update { it.copy(nameFilter = nameFilter) }
    }

    override fun refresh() {

    }
}
package com.skyecodes.vercors.component

import com.skyecodes.vercors.data.model.app.Instance
import kotlinx.coroutines.flow.StateFlow

interface InstancesComponent : Refreshable {
    val instances: StateFlow<List<Instance>?>
    val openNewInstanceDialog: () -> Unit
}

class DefaultInstancesComponent(
    override val instances: StateFlow<List<Instance>?>,
    override val openNewInstanceDialog: () -> Unit,
    componentContext: AppComponentContext
) : AppComponentContext by componentContext, InstancesComponent {
    override fun refresh() {

    }
}
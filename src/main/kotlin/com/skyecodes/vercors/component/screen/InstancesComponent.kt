package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
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
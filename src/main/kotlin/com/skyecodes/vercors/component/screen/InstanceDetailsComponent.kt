package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable
import com.skyecodes.vercors.data.model.app.Instance

interface InstanceDetailsComponent : Refreshable {
    val instance: Instance
}

class DefaultInstanceDetailsComponent(
    componentContext: AppComponentContext,
    override val instance: Instance
) : AbstractComponent(componentContext), InstanceDetailsComponent {
    override fun refresh() {
        TODO("Not yet implemented")
    }

}
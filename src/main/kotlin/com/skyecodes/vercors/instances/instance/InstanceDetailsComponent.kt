package com.skyecodes.vercors.instances.instance

import com.skyecodes.vercors.instances.Instance
import com.skyecodes.vercors.root.AbstractComponent
import com.skyecodes.vercors.root.AppComponentContext
import com.skyecodes.vercors.root.Refreshable

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
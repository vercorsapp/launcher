package app.vercors.instance.details

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.AppTab
import app.vercors.toolbar.ToolbarTitle

class InstanceDetailsComponentImpl(
    componentContext: AppComponentContext
) : AbstractAppComponent(componentContext), InstanceDetailsComponent {
    override val tab = AppTab.Instances
    override val isDefault = false
    override val title = ToolbarTitle.InstanceInfo

    override fun refresh() {
        // TODO
    }
}
package app.vercors.project

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.AppTab
import app.vercors.toolbar.ToolbarTitle

class ProjectDetailsComponentImpl(
    componentContext: AppComponentContext
) : AbstractAppComponent(componentContext), ProjectDetailsComponent {
    override val tab: AppTab = AppTab.Search
    override val isDefault: Boolean = false
    override val title: ToolbarTitle = ToolbarTitle.Search

    override fun refresh() {
        // TODO
    }
}
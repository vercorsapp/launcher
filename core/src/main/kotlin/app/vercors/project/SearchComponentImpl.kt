package app.vercors.project

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.AppTab
import app.vercors.toolbar.ToolbarTitle

class SearchComponentImpl(
    componentContext: AppComponentContext
) : AbstractAppComponent(componentContext), SearchComponent {
    override val tab = AppTab.Search
    override val isDefault = true
    override val title = ToolbarTitle.Search

    override fun refresh() {
        // TODO
    }
}
package com.skyecodes.vercors.projects

import com.skyecodes.vercors.root.AbstractComponent
import com.skyecodes.vercors.root.AppComponentContext
import com.skyecodes.vercors.root.Refreshable

interface SearchComponent : Refreshable

class DefaultSearchComponent(
    componentContext: AppComponentContext
) : AbstractComponent(componentContext), SearchComponent {
    override fun refresh() {

    }
}

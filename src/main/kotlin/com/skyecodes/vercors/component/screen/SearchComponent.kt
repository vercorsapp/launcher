package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable

interface SearchComponent : Refreshable

class DefaultSearchComponent(
    componentContext: AppComponentContext
) : AbstractComponent(componentContext), SearchComponent {
    override fun refresh() {

    }
}

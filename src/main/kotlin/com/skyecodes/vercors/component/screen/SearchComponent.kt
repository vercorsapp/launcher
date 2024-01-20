package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.Refreshable

interface SearchComponent : Refreshable

class DefaultSearchComponent(
    componentContext: AppComponentContext
) : AppComponentContext by componentContext, SearchComponent {
    override fun refresh() {

    }
}

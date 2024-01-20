package com.skyecodes.vercors.component

interface SearchComponent : Refreshable

class DefaultSearchComponent(
    componentContext: AppComponentContext
) : AppComponentContext by componentContext, SearchComponent {
    override fun refresh() {

    }
}

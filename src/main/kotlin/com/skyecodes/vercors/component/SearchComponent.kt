package com.skyecodes.vercors.component

interface SearchComponent

class DefaultSearchComponent(
    componentContext: AppComponentContext
) : AppComponentContext by componentContext, SearchComponent

package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AppComponentContext

interface AccountsComponent

class DefaultAccountsComponent(
    componentContext: AppComponentContext
) : AppComponentContext by componentContext, AccountsComponent

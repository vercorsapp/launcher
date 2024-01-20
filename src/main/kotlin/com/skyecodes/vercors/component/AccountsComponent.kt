package com.skyecodes.vercors.component

interface AccountsComponent

class DefaultAccountsComponent(
    componentContext: AppComponentContext
) : AppComponentContext by componentContext, AccountsComponent

package com.skyecodes.vercors.component.screen

import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext

interface AccountsComponent

class DefaultAccountsComponent(
    componentContext: AppComponentContext
) : AbstractComponent(componentContext), AccountsComponent

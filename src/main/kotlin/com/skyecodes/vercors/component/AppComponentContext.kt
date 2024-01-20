package com.skyecodes.vercors.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.skiko.MainUIDispatcher
import org.koin.core.Koin

interface AppComponentContext : ComponentContext {
    val koin: Koin
    val scope: CoroutineScope
}

class DefaultAppComponentContext(
    componentContext: ComponentContext,
    override val koin: Koin
) : AppComponentContext, ComponentContext by componentContext {
    override val scope = coroutineScope(MainUIDispatcher + SupervisorJob())
}
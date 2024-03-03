package app.vercors.common

import app.vercors.MainDispatcher
import app.vercors.di.DI
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppComponentContextImpl(
    componentContext: ComponentContext,
    override val di: DI
) : AppComponentContext, ComponentContext by componentContext {
    override val scope: CoroutineScope = coroutineScope(MainDispatcher + SupervisorJob())
}
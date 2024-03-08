package app.vercors.common

import app.vercors.di.DI
import app.vercors.di.inject
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope

interface AppComponentContext : ComponentContext, CoroutineScope {
    val di: DI

    fun appChildContext(key: String, lifecycle: Lifecycle? = null): AppComponentContext =
        appChildContext(childContext(key, lifecycle))
    fun appChildContext(context: ComponentContext): AppComponentContext = inject<AppComponentContext>(context, di)
}

inline fun <reified T : Any> AppComponentContext.inject(vararg args: Any): T = di.inject(*args)
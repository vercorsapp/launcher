package app.vercors.common

import app.vercors.di.DI
import app.vercors.di.inject
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.CoroutineScope

interface AppComponentContext : ComponentContext {
    val scope: CoroutineScope
    val di: DI

    fun appChildContext(key: String): AppComponentContext = appChildContext(childContext(key))

    fun appChildContext(context: ComponentContext): AppComponentContext = inject<AppComponentContext>(context, di)
}

inline fun <reified T : Any> AppComponentContext.inject(vararg args: Any): T = di.inject(*args)
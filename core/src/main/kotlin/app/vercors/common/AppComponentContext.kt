package app.vercors.common

import app.vercors.di.DI
import app.vercors.di.inject
import app.vercors.onError
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.ObserveLifecycleMode
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface AppComponentContext : ComponentContext, CoroutineScope {
    val di: DI

    fun appChildContext(key: String, lifecycle: Lifecycle? = null): AppComponentContext =
        appChildContext(childContext(key, lifecycle))
    fun appChildContext(context: ComponentContext): AppComponentContext = inject<AppComponentContext>(context, di)
    fun <C, T> childFactory(factory: (C, AppComponentContext) -> T): (C, ComponentContext) -> T =
        { configuration, componentContext ->
            factory(configuration, appChildContext(componentContext))
        }

    fun launchInLifecycle(
        mode: ObserveLifecycleMode = ObserveLifecycleMode.START_STOP,
        errorHandler: Job.(Throwable) -> Unit = {},
        observer: suspend CoroutineScope.() -> Unit,
    ) {
        var job: Job? = null

        when (mode) {
            ObserveLifecycleMode.CREATE_DESTROY ->
                lifecycle.subscribe(
                    onCreate = { job = launch { observer() }.onError(errorHandler) },
                    onDestroy = { job?.cancel() },
                )

            ObserveLifecycleMode.START_STOP ->
                lifecycle.subscribe(
                    onStart = { job = launch { observer() }.onError(errorHandler) },
                    onStop = { job?.cancel() },
                )

            ObserveLifecycleMode.RESUME_PAUSE ->
                lifecycle.subscribe(
                    onResume = { job = launch { observer() }.onError(errorHandler) },
                    onPause = { job?.cancel() },
                )
        }
    }

    fun <T> Flow<T>.collectInLifecycle(
        mode: ObserveLifecycleMode = ObserveLifecycleMode.START_STOP,
        errorHandler: Job.(Throwable) -> Unit = {},
        collector: FlowCollector<T>,
    ) {
        var job: Job? = null

        when (mode) {
            ObserveLifecycleMode.CREATE_DESTROY ->
                lifecycle.subscribe(
                    onCreate = { job = launch { collect(collector) }.onError(errorHandler) },
                    onDestroy = { job?.cancel() },
                )

            ObserveLifecycleMode.START_STOP ->
                lifecycle.subscribe(
                    onStart = { job = launch { collect(collector) }.onError(errorHandler) },
                    onStop = { job?.cancel() },
                )

            ObserveLifecycleMode.RESUME_PAUSE ->
                lifecycle.subscribe(
                    onResume = { job = launch { collect(collector) }.onError(errorHandler) },
                    onPause = { job?.cancel() },
                )
        }
    }

    fun <T> ReceiveChannel<T>.consumeInLifecycle(
        mode: ObserveLifecycleMode = ObserveLifecycleMode.START_STOP,
        errorHandler: Job.(Throwable) -> Unit = {},
        collector: (T) -> Unit,
    ) {
        var job: Job? = null

        when (mode) {
            ObserveLifecycleMode.CREATE_DESTROY ->
                lifecycle.subscribe(
                    onCreate = { job = launch { consumeEach(collector) }.onError(errorHandler) },
                    onDestroy = { job?.cancel() },
                )

            ObserveLifecycleMode.START_STOP ->
                lifecycle.subscribe(
                    onStart = { job = launch { consumeEach(collector) }.onError(errorHandler) },
                    onStop = { job?.cancel() },
                )

            ObserveLifecycleMode.RESUME_PAUSE ->
                lifecycle.subscribe(
                    onResume = { job = launch { consumeEach(collector) }.onError(errorHandler) },
                    onPause = { job?.cancel() },
                )
        }
    }

    fun <T : Any> Value<T>.toStateFlow(mode: ObserveLifecycleMode = ObserveLifecycleMode.START_STOP): StateFlow<T> {
        val mutableFlow = MutableStateFlow(value)
        subscribe(lifecycle, mode) { value -> mutableFlow.update { value } }
        return mutableFlow
    }
}

inline fun <reified T : Any> AppComponentContext.inject(vararg args: Any): T = di.inject(*args)
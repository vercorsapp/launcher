/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.common

import app.vercors.di.DI
import app.vercors.di.inject
import app.vercors.onError
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.decompose.value.ObserveLifecycleMode
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface AppComponentContext : GenericComponentContext<AppComponentContext> {
    val di: DI
    val localScope: CoroutineScope

    fun launchInLifecycle(
        mode: ObserveLifecycleMode = ObserveLifecycleMode.START_STOP,
        errorHandler: Job.(Throwable) -> Unit = {},
        observer: suspend CoroutineScope.() -> Unit,
    ) {
        var job: Job? = null

        when (mode) {
            ObserveLifecycleMode.CREATE_DESTROY ->
                lifecycle.subscribe(
                    onCreate = { job = localScope.launch { observer() }.onError(errorHandler) },
                    onDestroy = { job?.cancel() },
                )

            ObserveLifecycleMode.START_STOP ->
                lifecycle.subscribe(
                    onStart = { job = localScope.launch { observer() }.onError(errorHandler) },
                    onStop = { job?.cancel() },
                )

            ObserveLifecycleMode.RESUME_PAUSE ->
                lifecycle.subscribe(
                    onResume = { job = localScope.launch { observer() }.onError(errorHandler) },
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
                    onCreate = { job = localScope.launch { collect(collector) }.onError(errorHandler) },
                    onDestroy = { job?.cancel() },
                )

            ObserveLifecycleMode.START_STOP ->
                lifecycle.subscribe(
                    onStart = { job = localScope.launch { collect(collector) }.onError(errorHandler) },
                    onStop = { job?.cancel() },
                )

            ObserveLifecycleMode.RESUME_PAUSE ->
                lifecycle.subscribe(
                    onResume = { job = localScope.launch { collect(collector) }.onError(errorHandler) },
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
                    onCreate = { job = localScope.launch { consumeEach(collector) }.onError(errorHandler) },
                    onDestroy = { job?.cancel() },
                )

            ObserveLifecycleMode.START_STOP ->
                lifecycle.subscribe(
                    onStart = { job = localScope.launch { consumeEach(collector) }.onError(errorHandler) },
                    onStop = { job?.cancel() },
                )

            ObserveLifecycleMode.RESUME_PAUSE ->
                lifecycle.subscribe(
                    onResume = { job = localScope.launch { consumeEach(collector) }.onError(errorHandler) },
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
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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T> createLoadableStateFlow(): MutableStateFlow<Resource<T>> = MutableStateFlow(Resource.NotLoaded())

fun <T> StateFlow<Resource<T>>.toResultStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly
): StateFlow<T?> = map {
    when (it) {
        is Resource.Loaded -> it.result
        is Resource.Loading -> it.partialResult
        else -> null
    }
}.stateIn(scope, started, null)

suspend fun <T> MutableStateFlow<Resource<T>>.emitLoading(progress: Float, partial: T?) =
    emit(Resource.Loading(progress, partial))

fun <T> MutableStateFlow<Resource<T>>.updateLoading(progress: Float, updater: (T?) -> T?) = update {
    if (it is Resource.Loading) Resource.Loading(progress, updater(it.partialResult)) else it
}

suspend fun <T> MutableStateFlow<Resource<T>>.emitLoaded(value: T) = emit(Resource.Loaded(value))

fun <T> MutableStateFlow<Resource<T>>.updateLoaded(updater: (T) -> T) = update {
    if (it is Resource.Loaded) Resource.Loaded(updater(it.result)) else it
}

suspend fun <T> MutableStateFlow<Resource<T>>.emitErrored(error: Exception) = emit(Resource.Errored(error))

fun <T> MutableStateFlow<Resource<T>>.toLoaded() = update {
    if (it is Resource.Loading) Resource.Loaded(it.partialResult!!) else it
}

fun <T> MutableStateFlow<Resource<T>>.updateLoadedAndGet(updater: (T) -> T): Resource<T> = updateAndGet {
    if (it is Resource.Loaded) Resource.Loaded(updater(it.result)) else it
}
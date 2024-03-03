package app.vercors.di

import kotlinx.coroutines.Deferred

sealed interface DISingleton<T : Any> {
    data class Lazy<T : Any>(val value: T) : DISingleton<T>
    data class Async<T : Any>(val deferred: Deferred<T>) : DISingleton<T>
}
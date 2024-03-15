package app.vercors.di

import kotlin.reflect.KClass

interface DI {
    fun <T : Any> inject(kClass: KClass<T>, vararg args: Any): T
    suspend fun awaitInit()
}

inline fun <reified T : Any> DI.inject(vararg args: Any): T = inject(T::class, *args)

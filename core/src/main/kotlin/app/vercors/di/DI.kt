package app.vercors.di

import kotlin.reflect.KClass

interface DI {
    fun <T : Any> inject(kClass: KClass<T>, vararg args: Any): T
}

inline fun <reified T : Any> DI.inject(vararg args: Any): T = inject(T::class, *args)

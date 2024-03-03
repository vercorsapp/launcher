package app.vercors.di

import kotlin.reflect.KClass

interface DIBuilder {
    fun <T : Any> lazySingle(kClass: KClass<T>, provider: DIInjectionContext.() -> T)
    fun <T : Any> single(kClass: KClass<T>, provider: DI.() -> T)
    fun <T : Any> factory(kClass: KClass<T>, provider: DIInjectionContext.() -> T)
    fun build(): DI
}

inline fun <reified T : Any> DIBuilder.lazySingle(noinline provider: DIInjectionContext.() -> T) =
    lazySingle(T::class, provider)

inline fun <reified T : Any> DIBuilder.single(noinline provider: DI.() -> T) = single(T::class, provider)

inline fun <reified T : Any> DIBuilder.factory(noinline provider: DIInjectionContext.() -> T) =
    factory(T::class, provider)
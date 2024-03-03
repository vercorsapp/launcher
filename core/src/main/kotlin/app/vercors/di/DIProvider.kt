package app.vercors.di

sealed interface DIProvider<T : Any> {
    data class LazySingle<T : Any>(val provider: DIInjectionContext.() -> T) : DIProvider<T>
    data class Factory<T : Any>(val provider: DIInjectionContext.() -> T) : DIProvider<T>
    data class Single<T : Any>(val provider: DI.() -> T) : DIProvider<T>
}

package app.vercors.di

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class DefaultDIBuilder(
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext
) : DIBuilder {
    private val providerMap = mutableMapOf<KClass<*>, DIProvider<*>>()

    override fun <T : Any> lazySingle(kClass: KClass<T>, provider: DIInjectionContext.() -> T) {
        providerMap[kClass] = DIProvider.LazySingle(provider)
    }

    override fun <T : Any> single(kClass: KClass<T>, provider: suspend DI.() -> T) {
        providerMap[kClass] = DIProvider.Single(provider)
    }

    override fun <T : Any> factory(kClass: KClass<T>, provider: DIInjectionContext.() -> T) {
        providerMap[kClass] = DIProvider.Factory(provider)
    }

    override fun build(): DI = DefaultDI(coroutineScope, coroutineContext, providerMap)
}
package app.vercors.di

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger { }

class DefaultDI(
    coroutineScope: CoroutineScope,
    coroutineContext: CoroutineContext,
    private val providerMap: Map<KClass<*>, DIProvider<*>>
) : DI {
    private val singletonMap = ConcurrentHashMap<KClass<*>, DISingleton<*>>(mapOf(DI::class to DISingleton.Lazy(this)))

    init {
        coroutineScope.launch(coroutineContext) {
            logger.debug { "Initializing singletons" }
            measureTimeMillis {
                providerMap.filterValues { it is DIProvider.Single }.forEach { (kClass, provider) ->
                    singletonMap[kClass] = DISingleton.Async(coroutineScope.async(coroutineContext) {
                        provide(kClass) { (provider as DIProvider.Single).provider(this@DefaultDI) }
                    })
                }
                singletonMap.values.filterIsInstance<DISingleton.Async<*>>().map { it.deferred }.awaitAll()
            }.let { logger.debug { "Initialized singletons in ${it}ms" } }
        }
    }

    @Suppress("unchecked_cast")
    override fun <T : Any> inject(kClass: KClass<T>, vararg args: Any): T = when (val provider = providerMap[kClass]) {
        is DIProvider.Factory -> {
            val context = DefaultDIInjectionContext(this, args)
            provide(kClass) { provider.provider(context) }
        }

        is DIProvider.LazySingle -> {
            (singletonMap.getOrPut(kClass) {
                val context = DefaultDIInjectionContext(this, args)
                DISingleton.Lazy(provide(kClass) { provider.provider(context) })
            } as DISingleton.Lazy).value as T
        }

        is DIProvider.Single -> runBlocking { (singletonMap[kClass] as DISingleton.Async).deferred.await() } as T
        null -> throw IllegalArgumentException("Class $kClass is not registered in DI")
    }

    @Suppress("unchecked_cast")
    private fun <T : Any> provide(kClass: KClass<T>, provider: suspend () -> Any): T {
        logger.debug { "Providing $kClass" }
        val result: T
        measureTimeMillis {
            runBlocking {
            result = provider() as T
            }
        }.let { logger.debug { "Provided $kClass in ${it}ms" } }
        return result
    }
}

fun DI(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: DIBuilder.() -> Unit
): DI = DefaultDIBuilder(coroutineScope, coroutineContext).apply(block).build()
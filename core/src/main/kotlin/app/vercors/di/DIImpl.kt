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

package app.vercors.di

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger { }

internal class DIImpl(
    coroutineScope: CoroutineScope,
    coroutineContext: CoroutineContext,
    private val providerMap: Map<KClass<*>, DIProvider<*>>
) : DI {
    private val singletonMap = ConcurrentHashMap<KClass<*>, DISingleton<*>>(mapOf(DI::class to DISingleton.Lazy(this)))
    private val initJob: Job

    init {
        initJob = coroutineScope.launch(coroutineContext) {
            logger.debug { "Initializing singletons" }
            measureTimeMillis {
                providerMap.filterValues { it is DIProvider.Single }.forEach { (kClass, provider) ->
                    singletonMap[kClass] = DISingleton.Async(coroutineScope.async(coroutineContext) {
                        provide(kClass) { (provider as DIProvider.Single).provider(this@DIImpl) }
                    })
                }
                singletonMap.values.filterIsInstance<DISingleton.Async<*>>().map { it.deferred }.awaitAll()
            }.let { logger.debug { "Initialized singletons in ${it}ms" } }
        }
    }

    @Suppress("unchecked_cast")
    override fun <T : Any> inject(kClass: KClass<T>, vararg args: Any): T = when (val provider = providerMap[kClass]) {
        is DIProvider.Factory -> {
            val context = DIInjectionContextImpl(this, args)
            provide(kClass) { provider.provider(context) }
        }

        is DIProvider.LazySingle -> {
            (singletonMap.getOrPut(kClass) {
                val context = DIInjectionContextImpl(this, args)
                DISingleton.Lazy(provide(kClass) { provider.provider(context) })
            } as DISingleton.Lazy).value as T
        }

        is DIProvider.Single -> runBlocking { (singletonMap[kClass] as DISingleton.Async).deferred.await() } as T
        null -> throw IllegalArgumentException("Class $kClass is not registered in DI")
    }

    override suspend fun awaitInit() = initJob.join()

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
): DI = DIBuilderImpl(coroutineScope, coroutineContext).apply(block).build()
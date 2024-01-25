package com.skyecodes.vercors.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.skiko.MainUIDispatcher
import org.koin.core.Koin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

private val logger = KotlinLogging.logger { }

interface AppComponentContext : ComponentContext {
    val koin: Koin
    val scope: CoroutineScope
}

class DefaultAppComponentContext(
    componentContext: ComponentContext,
    override val koin: Koin
) : AppComponentContext, ComponentContext by componentContext {
    override val scope = coroutineScope(MainUIDispatcher + SupervisorJob())
}

/**
 * @see Koin.inject
 */
inline fun <reified T : Any> AppComponentContext.inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> = koin.inject(qualifier, mode, parameters)

/**
 * @see Koin.injectOrNull
 */
inline fun <reified T : Any> AppComponentContext.injectOrNull(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null,
): Lazy<T?> = koin.injectOrNull(qualifier, mode, parameters)

/**
 * @see Koin.get
 */
inline fun <reified T : Any> AppComponentContext.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T = koin.get(qualifier, parameters)

/**
 * @see Koin.getOrNull
 */
inline fun <reified T : Any> AppComponentContext.getOrNull(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T? = koin.getOrNull(qualifier, parameters)
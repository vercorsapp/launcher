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

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

internal class DIBuilderImpl(
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

    override fun build(): DI = DIImpl(coroutineScope, coroutineContext, providerMap)
}
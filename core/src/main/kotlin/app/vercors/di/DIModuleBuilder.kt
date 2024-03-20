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

import kotlin.reflect.KClass

interface DIModuleBuilder {
    fun <T : Any> lazySingle(kClass: KClass<T>, provider: DIInjectionContext.() -> T)
    fun <T : Any> single(kClass: KClass<T>, provider: suspend DI.() -> T)
    fun <T : Any> factory(kClass: KClass<T>, provider: DIInjectionContext.() -> T)
    fun build(): DIModule
}

inline fun <reified T : Any> DIModuleBuilder.lazySingle(noinline provider: DIInjectionContext.() -> T) =
    lazySingle(T::class, provider)

inline fun <reified T : Any> DIModuleBuilder.single(noinline provider: suspend DI.() -> T) = single(T::class, provider)

inline fun <reified T : Any> DIModuleBuilder.factory(noinline provider: DIInjectionContext.() -> T) =
    factory(T::class, provider)
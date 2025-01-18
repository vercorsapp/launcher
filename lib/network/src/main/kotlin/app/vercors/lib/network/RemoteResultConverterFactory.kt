/*
 * Copyright (c) 2025 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.lib.network

import app.vercors.lib.domain.DomainError
import app.vercors.lib.domain.RemoteResult
import app.vercors.lib.domain.Result
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

private val logger = KotlinLogging.logger { }

object RemoteResultConverterFactory : Converter.Factory {
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? =
        if (typeData.typeInfo.type == RemoteResult::class) ResponseConverter<Any>(typeData) else null

    private class ResponseConverter<T : Any>(
        private val typeData: TypeData
    ) : Converter.SuspendResponseConverter<HttpResponse, RemoteResult<T>> {
        @Suppress("unchecked_cast")
        override suspend fun convert(result: KtorfitResult): RemoteResult<T> = when (result) {
            is KtorfitResult.Success -> {
                try {
                    Result.Success(result.response.body(typeData.typeArgs.first().typeInfo))
                } catch (e: Exception) {
                    currentCoroutineContext().ensureActive()
                    logger.error(e) { "Error parsing network response" }
                    Result.Error(DomainError.SerializationError)
                }
            }

            is KtorfitResult.Failure -> {
                logger.error(result.throwable) { "Network error" }
                Result.Error(DomainError.NetworkError)
            }
        }
    }
}
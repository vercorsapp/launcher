/*
 * Copyright (c) 2024-2025 skyecodes
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

package app.vercors.launcher.core.meta

import app.vercors.launcher.core.domain.DomainError
import app.vercors.launcher.core.domain.RemoteResult
import app.vercors.launcher.core.domain.Result
import app.vercors.meta.MetaError
import app.vercors.meta.MetaResponse
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.Message
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.statement.*

private val logger = KotlinLogging.logger { }

class MetaRemoteResultConverterFactory() : Converter.Factory {
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? =
        if (typeData.typeInfo.type == RemoteResult::class) ResponseConverter<Message>(typeData) else null

    private class ResponseConverter<T : Message>(
        private val typeData: TypeData
    ) : Converter.SuspendResponseConverter<HttpResponse, RemoteResult<T>> {
        @Suppress("unchecked_cast")
        override suspend fun convert(result: KtorfitResult): RemoteResult<T> = when (result) {
            is KtorfitResult.Success -> {
                try {
                    val response = MetaResponse.parseFrom(result.response.bodyAsBytes())
                    if (response.hasError()) Result.Error(handleError(response.error)) else {
                        val success = response.success.unpack(typeData.typeArgs.first().typeInfo.type.java as Class<T>)
                        Result.Success(success)
                    }
                } catch (e: InvalidProtocolBufferException) {
                    logger.error(e) { "Error parsing network response" }
                    Result.Error(DomainError.SerializationError)
                }
            }

            is KtorfitResult.Failure -> {
                logger.error(result.throwable) { "Network error" }
                Result.Error(DomainError.Remote.NetworkError)
            }
        }

        private fun handleError(error: MetaError): DomainError.Remote {
            // TODO improve error handling
            return DomainError.UnknownError
        }
    }
}
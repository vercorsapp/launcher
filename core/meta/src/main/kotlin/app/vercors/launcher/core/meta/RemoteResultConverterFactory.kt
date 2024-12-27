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

class RemoteResultConverterFactory() : Converter.Factory {
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
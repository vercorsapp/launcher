package app.vercors.launcher.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface Resource<out D, out E : DomainError> {
    data object Loading : Resource<Nothing, Nothing>
    data class Success<out D>(val value: D) : Resource<D, Nothing>
    data class Error<out E : DomainError>(val error: E) : Resource<Nothing, E>
}

fun <D, E : DomainError> observeResource(result: suspend () -> Result<D, E>): Flow<Resource<D, E>> = flow {
    emit(Resource.Loading)
    val res = result()
    when (res) {
        is Result.Error -> emit(Resource.Error(res.error))
        is Result.Success -> emit(Resource.Success(res.value))
    }
}
package app.vercors.launcher.core.domain

sealed interface Result<out D, out E : DomainError> {
    data class Success<out D>(val value: D) : Result<D, Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>
}

fun <D, E : DomainError, T> Result<D, E>.map(mapper: (D) -> T): Result<T, E> = when (this) {
    is Result.Success -> Result.Success(mapper(value))
    is Result.Error -> this
}

typealias RemoteResult<D> = Result<D, DomainError.Remote>

typealias LocalResult<D> = Result<D, DomainError.Local>
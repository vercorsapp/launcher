package app.vercors.launcher.core.domain.util

typealias DomainError = Error

sealed interface Result<out T, out E : Error> {
    data class Success<out T>(val value: T) : Result<T, Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>
}

fun <T, E : Error, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(transform(value))
    }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(value)
    return this
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) action(error)
    return this
}
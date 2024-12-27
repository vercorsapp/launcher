package app.vercors.launcher.core.domain

sealed interface DomainError {
    sealed interface Remote : DomainError {
        data object NetworkError : Remote
    }

    sealed interface Local : DomainError

    data object SerializationError : Remote, Local
    data object UnknownError : Remote, Local
}
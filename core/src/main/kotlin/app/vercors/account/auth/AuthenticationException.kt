package app.vercors.account.auth

class AuthenticationException(message: String? = "An error has occurred", cause: Throwable? = null) :
    Exception(message, cause)
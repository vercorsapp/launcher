package app.vercors.account.auth

import app.vercors.account.AccountData

sealed interface AuthenticationState {
    data class Waiting(val url: String) : AuthenticationState
    data class Progress(val progress: Float) : AuthenticationState
    data class Success(val account: AccountData) : AuthenticationState
    data object Closed : AuthenticationState
}


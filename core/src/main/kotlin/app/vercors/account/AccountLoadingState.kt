package app.vercors.account

sealed interface AccountLoadingState {
    data object NotLoaded : AccountLoadingState
    data class Loaded(val data: AccountListData) : AccountLoadingState
    data class Error(val error: Throwable) : AccountLoadingState
}

package app.vercors.dialog.login

import app.vercors.account.AccountData

data class LoginDialogUiState(
    val url: String? = null,
    val progress: Float = -1f,
    val error: Throwable? = null,
    val account: AccountData? = null
) {
    val canClose = account != null || error != null
    val isWaitingLogin = progress < 0 && error == null
    val hasUrl = url != null && isWaitingLogin && error == null
    val isSuccess = account != null && error == null
}

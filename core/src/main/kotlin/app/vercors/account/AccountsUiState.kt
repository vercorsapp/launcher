package app.vercors.account

data class AccountsUiState(
    val isPopupOpen: Boolean = false,
    val data: AccountListData = AccountListData()
)

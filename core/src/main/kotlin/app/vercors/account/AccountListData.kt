package app.vercors.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountListData(
    val selected: String? = null,
    val accounts: List<AccountData> = emptyList()
)

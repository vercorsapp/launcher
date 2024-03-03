package app.vercors.account

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface AccountRepository {
    suspend fun loadAccounts(context: CoroutineContext = Dispatchers.IO): AccountListData
    suspend fun saveAccounts(accounts: AccountListData, context: CoroutineContext = Dispatchers.IO)
}
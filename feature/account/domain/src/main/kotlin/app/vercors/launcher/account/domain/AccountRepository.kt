package app.vercors.launcher.account.domain

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeAccounts(): Flow<List<Account>>
    suspend fun addAccount(account: Account)
    suspend fun removeAccount(account: Account)
}
package app.vercors.launcher.settings.domain.repository

import app.vercors.launcher.settings.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeAccounts(): Flow<List<Account>>
    suspend fun addAccount(account: Account)
    suspend fun removeAccount(account: Account)
}
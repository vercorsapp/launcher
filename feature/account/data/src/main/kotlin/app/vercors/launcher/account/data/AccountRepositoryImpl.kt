package app.vercors.launcher.account.data

import app.vercors.launcher.account.domain.Account
import app.vercors.launcher.account.domain.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AccountRepositoryImpl(
    private val dao: AccountDao
) : AccountRepository {
    override fun observeAccounts(): Flow<List<Account>> {
        return dao.observeAccounts()
            .map { accounts -> accounts.map { it.toAccount() } }
            .distinctUntilChanged()
    }

    override suspend fun addAccount(account: Account) {
        dao.upsert(account.toEntity())
    }

    override suspend fun removeAccount(account: Account) {
        dao.delete(account.toEntity())
    }
}
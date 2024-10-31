package app.vercors.launcher.account.data.repository

import app.vercors.launcher.account.data.dao.AccountDao
import app.vercors.launcher.account.data.mapper.AccountEntityMapper
import app.vercors.launcher.account.domain.model.Account
import app.vercors.launcher.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AccountRepositoryImpl(
    private val dao: AccountDao,
    private val mapper: AccountEntityMapper
) : AccountRepository {
    override fun observeAccounts(): Flow<List<Account>> {
        return dao.observeAccounts()
            .map { accounts -> accounts.map { mapper.fromData(it) } }
            .distinctUntilChanged()
    }

    override suspend fun addAccount(account: Account) {
        dao.upsert(mapper.toData(account))
    }

    override suspend fun removeAccount(account: Account) {
        dao.delete(mapper.toData(account))
    }
}
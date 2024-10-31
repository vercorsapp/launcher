package app.vercors.launcher.account.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import app.vercors.launcher.account.data.model.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun observeAccounts(): Flow<List<AccountEntity>>

    @Upsert
    suspend fun upsert(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)
}
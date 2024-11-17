package app.vercors.launcher.account.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
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
package app.vercors.account.auth

import app.vercors.account.AccountData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface AuthenticationService {
    fun startAuthentication(context: CoroutineContext = Dispatchers.IO): Flow<AuthenticationState>
    fun cancelAuthentication()
    suspend fun validateToken(account: AccountData?, context: CoroutineContext = Dispatchers.IO): AccountData?
}
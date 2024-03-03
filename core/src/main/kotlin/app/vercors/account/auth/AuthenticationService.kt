package app.vercors.account.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface AuthenticationService {
    fun startAuthentication(context: CoroutineContext = Dispatchers.IO): Flow<AuthenticationState>
    suspend fun validateToken(): Boolean
}
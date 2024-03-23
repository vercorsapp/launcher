/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.account.auth

import app.vercors.account.Account
import app.vercors.encodeBase64
import app.vercors.sha256
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import java.util.*

private val logger = KotlinLogging.logger {}

internal class AuthenticateUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthenticateUseCase {
    override fun invoke(): Flow<AuthenticationState> = channelFlow {
        val emitter: suspend (AuthenticationState) -> Unit = { send(it) }
        withContext(ioDispatcher) {
            logger.info { "Starting authentication flow" }
            var progress = 0
            val state = UUID.randomUUID().toString()
            val codeVerifier = UUID.randomUUID().toString()
            val codeChallenge = codeVerifier.sha256().encodeBase64().take(43)
            val (authorizationCode, port) = authenticationRepository.getAuthorizationCode(emitter, state, codeChallenge)
            progress(emitter, ++progress)
            val (msAccessToken, refreshToken) = authenticationRepository.getMicrosoftAccessToken(
                codeVerifier,
                authorizationCode,
                port
            )
            progress(emitter, ++progress)
            val (xblToken, userHash) = authenticationRepository.getXblToken(msAccessToken)
            progress(emitter, ++progress)
            val xstsToken = authenticationRepository.getXstsToken(xblToken, userHash)
            progress(emitter, ++progress)
            val token = authenticationRepository.getMinecraftAccessToken(userHash, xstsToken)
            progress(emitter, ++progress)
            val (uuid, username) = authenticationRepository.getMinecraftProfile(token)
            progress(emitter, ++progress)
            emitter(AuthenticationState.Success(Account(username, uuid, refreshToken, token)))
        }
    }

    private suspend fun progress(emitter: suspend (AuthenticationState) -> Unit, progress: Int) {
        emitter(AuthenticationState.Progress(progress.toFloat() / 6))
    }
}
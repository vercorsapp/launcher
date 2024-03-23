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

internal class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource
) : AuthenticationRepository {
    override suspend fun getAuthorizationCode(
        emitter: suspend (AuthenticationState) -> Unit,
        state: String,
        codeChallenge: String
    ): AuthorizationCodeResult = authenticationDataSource.getAuthorizationCode(emitter, state, codeChallenge)

    override suspend fun getMicrosoftAccessToken(
        codeVerifier: String,
        authorizationCode: String,
        port: Int
    ): MicrosoftAccessTokenResult =
        authenticationDataSource.getMicrosoftAccessToken(codeVerifier, authorizationCode, port)

    override suspend fun refreshMicrosoftAccessToken(refreshToken: String): MicrosoftAccessTokenResult =
        authenticationDataSource.refreshMicrosoftAccessToken(refreshToken)

    override suspend fun getXblToken(xblAccessToken: String): XblTokenResult =
        authenticationDataSource.getXblToken(xblAccessToken)

    override suspend fun getXstsToken(xblToken: String, userHash: String): String =
        authenticationDataSource.getXstsToken(xblToken, userHash)

    override suspend fun getMinecraftAccessToken(userHash: String, xstsToken: String): String =
        authenticationDataSource.getMinecraftAccessToken(userHash, xstsToken)

    override suspend fun getMinecraftProfile(mcAccessToken: String): MinecraftProfileResult =
        authenticationDataSource.getMinecraftProfile(mcAccessToken)

    override suspend fun cancelAuthentication() = authenticationDataSource.cancelAuthentication()
}
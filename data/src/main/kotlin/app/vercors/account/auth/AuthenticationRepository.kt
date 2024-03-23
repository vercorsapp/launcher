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

interface AuthenticationRepository {
    suspend fun getAuthorizationCode(
        emitter: suspend (AuthenticationState) -> Unit,
        state: String,
        codeChallenge: String
    ): AuthorizationCodeResult

    suspend fun getMicrosoftAccessToken(
        codeVerifier: String,
        authorizationCode: String,
        port: Int
    ): MicrosoftAccessTokenResult

    suspend fun refreshMicrosoftAccessToken(refreshToken: String): MicrosoftAccessTokenResult
    suspend fun getXblToken(xblAccessToken: String): XblTokenResult
    suspend fun getXstsToken(xblToken: String, userHash: String): String
    suspend fun getMinecraftAccessToken(userHash: String, xstsToken: String): String
    suspend fun getMinecraftProfile(mcAccessToken: String): MinecraftProfileResult
    suspend fun cancelAuthentication()
}
/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.meta.auth

import org.koin.core.annotation.Property
import org.koin.core.annotation.Single

@Single
class AuthServiceImpl(@Property("microsoftClientId") private val microsoftClientId: String) : AuthService {
    override fun getAuthUrl(port: String, state: String, codeChallenge: String): String =
        "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?" +
                "client_id=$microsoftClientId" +
                "&response_type=code" +
                "&redirect_uri=http%3A%2F%2Flocalhost%3A$port" +
                "&response_mode=query" +
                "&scope=XboxLive.signin%20offline_access" +
                "&state=$state" +
                "&prompt=select_account" +
                "&code_challenge=$codeChallenge" +
                "&code_challenge_method=S256"

}
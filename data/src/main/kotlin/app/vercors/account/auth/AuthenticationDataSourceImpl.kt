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

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*

private val logger = KotlinLogging.logger {}

internal class AuthenticationDataSourceImpl(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthenticationDataSource {
    private var server: CIOApplicationEngine? = null

    override suspend fun getAuthorizationCode(
        emitter: suspend (AuthenticationState) -> Unit,
        state: String,
        codeChallenge: String
    ): AuthorizationCodeResult = coroutineScope {
        logger.info { "Getting authorization code" }
        var authorizationCode: String? = null
        var responseState: String? = null
        var error: String? = null
        server = embeddedServer(CIO, 0) {
            routing {
                get("/") {
                    if (this.context.parameters.contains("code")) {
                        authorizationCode = this.context.parameters["code"]
                        responseState = this.context.parameters["state"]
                        call.respondText("Authentication success! You can now close this window.")
                    } else if (this.context.parameters.contains("error")) {
                        val errorCode = this.context.parameters["error"]!!
                        val errorDescription = this.context.parameters["error_description"]!!
                        error = "$errorCode: $errorDescription"
                        call.respondText(error!!)
                    } else {
                        error = "Authentication failed"
                        call.respondText(error!!)
                    }
                }
            }
        }

        val result = runCatching {
            server!!.start()
            val port = server!!.resolvedConnectors()[0].port
            emitter(
                AuthenticationState.Waiting(
                    "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?" +
                            "client_id=$clientId" +
                            "&response_type=code" +
                            "&redirect_uri=http%3A%2F%2Flocalhost%3A$port" +
                            "&response_mode=query" +
                            "&scope=XboxLive.signin%20offline_access" +
                            "&state=$state" +
                            "&prompt=select_account" +
                            "&code_challenge=$codeChallenge" +
                            "&code_challenge_method=S256"
                )
            )
            while (authorizationCode == null && error == null) {
                delay(250)
                ensureActive()
            }
            error?.let { throw AuthenticationException(it) }
            if (state != responseState) throw AuthenticationException("Response state doesn't match")
            AuthorizationCodeResult(authorizationCode!!, port)
        }
        stopServer()
        result.getOrThrow()
    }

    override suspend fun cancelAuthentication() = stopServer()

    private suspend fun stopServer(): Unit = withContext(ioDispatcher) {
        server?.let {
            it.stop()
            server = null
            logger.debug { "Server stopped" }
        }
    }

    override suspend fun getMicrosoftAccessToken(
        codeVerifier: String,
        authorizationCode: String,
        port: Int
    ): MicrosoftAccessTokenResult {
        logger.info { "Getting access token" }
        return getMicrosoftAccessTokenWithParams(
            "code" to authorizationCode,
            "redirect_uri" to "http://localhost:$port",
            "grant_type" to "authorization_code",
            "code_verifier" to codeVerifier,
        )
    }

    override suspend fun refreshMicrosoftAccessToken(refreshToken: String): MicrosoftAccessTokenResult {
        logger.info { "Refreshing access token" }
        return getMicrosoftAccessTokenWithParams(
            "refresh_token" to refreshToken,
            "grant_type" to "refresh_token"
        )
    }

    private suspend fun getMicrosoftAccessTokenWithParams(vararg params: Pair<String, String>): MicrosoftAccessTokenResult {
        val tokenResult = httpClient.submitForm(
            url = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token",
            formParameters = parameters {
                set("client_id", clientId)
                set("scope", "XboxLive.signin offline_access")
                params.forEach { (a, b) -> set(a, b) }
            }
        ).body<JsonObject>()
        val xblAccessToken = tokenResult["access_token"]?.string
        val refreshToken = tokenResult["refresh_token"]?.string
        if (xblAccessToken == null || refreshToken == null) {
            if (tokenResult.containsKey("error")) {
                val error = tokenResult.getValue("error").string
                val errorDescription = tokenResult.getValue("error_description").string
                throw AuthenticationException("$error: $errorDescription")
            } else {
                invalidResponse(tokenResult)
            }
        }
        return MicrosoftAccessTokenResult(xblAccessToken, refreshToken)
    }

    private fun invalidResponse(result: JsonObject): Nothing {
        logger.warn { "Invalid response: $result" }
        throw AuthenticationException("Invalid response, see logs for more info")
    }

    override suspend fun getXblToken(xblAccessToken: String): XblTokenResult {
        logger.info { "Authenticating with Xbox Live" }
        val xboxResult = httpClient.post("https://user.auth.xboxlive.com/user/authenticate") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("Properties", buildJsonObject {
                    put("AuthMethod", "RPS")
                    put("SiteName", "user.auth.xboxlive.com")
                    put("RpsTicket", "d=$xblAccessToken")
                })
                put("RelyingParty", "http://auth.xboxlive.com")
                put("TokenType", "JWT")
            })
        }.body<JsonObject>()
        val xblToken = xboxResult["Token"]?.string
        val userHash = xboxResult["DisplayClaims"]?.jsonObject?.get("xui")
            ?.jsonArray?.get(0)?.jsonObject?.get("uhs")?.string
        if (xblToken == null || userHash == null) invalidResponse(xboxResult)
        return XblTokenResult(xblToken, userHash)
    }

    override suspend fun getXstsToken(xblToken: String, userHash: String): String {
        logger.info { "Obtaining XSTS token" }
        val xstsResult = httpClient.post("https://xsts.auth.xboxlive.com/xsts/authorize") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("Properties", buildJsonObject {
                    put("SandboxId", "RETAIL")
                    put("UserTokens", buildJsonArray { add(xblToken) })
                })
                put("RelyingParty", "rp://api.minecraftservices.com/")
                put("TokenType", "JWT")
            })
        }.body<JsonObject>()
        val xstsToken = xstsResult["Token"]?.string
        val userHash2 = xstsResult["DisplayClaims"]?.jsonObject?.get("xui")
            ?.jsonArray?.get(0)?.jsonObject?.get("uhs")?.string

        if (xstsToken == null) invalidResponse(xstsResult)
        if (userHash != userHash2) throw AuthenticationException("User hashes don't match")
        return xstsToken
    }

    override suspend fun getMinecraftAccessToken(userHash: String, xstsToken: String): String {
        logger.info { "Authenticating with Minecraft" }
        val minecraftResult = httpClient.post("https://api.minecraftservices.com/authentication/login_with_xbox") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("identityToken", "XBL3.0 x=$userHash;$xstsToken")
            })
        }.body<JsonObject>()
        val mcAccessToken = minecraftResult["access_token"]?.string
            ?: if (minecraftResult.containsKey("errorMessage")) throw AuthenticationException(minecraftResult["errorMessage"]?.string)
            else invalidResponse(minecraftResult)
        return mcAccessToken
    }

    override suspend fun getMinecraftProfile(mcAccessToken: String): MinecraftProfileResult {
        logger.info { "Getting Minecraft profile" }
        val profileResult = httpClient.get("https://api.minecraftservices.com/minecraft/profile") {
            bearerAuth(mcAccessToken)
            accept(ContentType.Application.Json)
        }.body<JsonObject>()
        val uuid = profileResult["id"]?.string
        val username = profileResult["name"]?.string
        if (uuid == null || username == null) invalidResponse(profileResult)
        return MinecraftProfileResult(uuid, username)
    }

    private val JsonElement.string get() = jsonPrimitive.content
}
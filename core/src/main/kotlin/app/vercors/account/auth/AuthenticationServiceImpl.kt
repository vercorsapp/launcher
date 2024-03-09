package app.vercors.account.auth

import app.vercors.account.AccountData
import app.vercors.encodeBase64
import app.vercors.sha256
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.json.*
import java.time.Instant
import java.util.*
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger { }

class AuthenticationServiceImpl(
    coroutineScope: CoroutineScope,
    private val httpClient: HttpClient,
    private val clientId: String
) : AuthenticationService, CoroutineScope by coroutineScope {
    private var server: CIOApplicationEngine? = null

    override fun startAuthentication(context: CoroutineContext): Flow<AuthenticationState> = channelFlow {
        val emitter: suspend (AuthenticationState) -> Unit = { send(it) }
        withContext(context) {
            var progress = 0
            val state = UUID.randomUUID().toString()
            val codeVerifier = UUID.randomUUID().toString()
            val codeChallenge = codeVerifier.sha256().encodeBase64().take(43)
            val (authorizationCode, port) = getAuthorizationCode(emitter, state, codeChallenge, context)
            progress(emitter, ++progress)
            val (msAccessToken, refreshToken) = getMicrosoftAccessToken(codeVerifier, authorizationCode, port)
            progress(emitter, ++progress)
            val (xblToken, userHash) = getXblToken(msAccessToken)
            progress(emitter, ++progress)
            val xstsToken = getXstsToken(xblToken, userHash)
            progress(emitter, ++progress)
            val (token, exp) = getMinecraftAccessToken(userHash, xstsToken)
            progress(emitter, ++progress)
            val (uuid, username) = getMinecraftProfile(token)
            progress(emitter, ++progress)
            emitter(AuthenticationState.Success(AccountData(username, uuid, refreshToken, token, exp)))
        }
    }

    override fun cancelAuthentication() {
        launch { stopServer() }
    }

    private suspend fun stopServer(context: CoroutineContext = Dispatchers.IO) = withContext(context) {
        server?.let {
            it.stop()
            server = null
            logger.debug { "Server stopped" }
        }
    }

    override suspend fun validateToken(): Boolean {
        return true // TODO
    }

    private suspend fun getAuthorizationCode(
        emitter: suspend (AuthenticationState) -> Unit,
        state: String,
        codeChallenge: String,
        context: CoroutineContext
    ): Pair<String, Int> = coroutineScope {
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
            authorizationCode!! to port
        }
        stopServer(context)
        result.getOrThrow()
    }

    private suspend fun getMicrosoftAccessToken(
        codeVerifier: String,
        authorizationCode: String,
        port: Int
    ): Pair<String, String> {
        logger.info { "Getting access token" }
        return getMicrosoftAccessTokenWithParams(
            "code" to authorizationCode,
            "redirect_uri" to "http://localhost:$port",
            "grant_type" to "authorization_code",
            "code_verifier" to codeVerifier,
        )
    }

    private suspend fun refreshMicrosoftAccessToken(refreshToken: String): Pair<String, String> {
        logger.info { "Refreshing access token" }
        return getMicrosoftAccessTokenWithParams(
            "refresh_token" to refreshToken,
            "grant_type" to "refresh_token"
        )
    }

    private suspend fun getMicrosoftAccessTokenWithParams(vararg params: Pair<String, String>): Pair<String, String> {
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
        return xblAccessToken to refreshToken
    }

    private suspend fun getXblToken(
        xblAccessToken: String
    ): Pair<String, String> {
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
        return xblToken to userHash
    }

    private fun invalidResponse(result: JsonObject): Nothing {
        logger.error { "Invalid response: $result" }
        throw AuthenticationException("Invalid response, see logs for more info")
    }

    private suspend fun getXstsToken(
        xblToken: String,
        userHash: String
    ): String {
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

    private suspend fun getMinecraftAccessToken(
        userHash: String,
        xstsToken: String
    ): Pair<String, Instant> {
        logger.info { "Authenticating with Minecraft" }
        val minecraftResult = httpClient.post("https://api.minecraftservices.com/authentication/login_with_xbox") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("identityToken", "XBL3.0 x=$userHash;$xstsToken")
            })
        }.body<JsonObject>()
        val mcAccessToken = minecraftResult["access_token"]?.string
        val expiresIn = minecraftResult["expires_in"]?.jsonPrimitive?.int
        if (mcAccessToken == null || expiresIn == null) {
            if (minecraftResult.containsKey("errorMessage")) throw AuthenticationException(minecraftResult["errorMessage"]?.string)
            else invalidResponse(minecraftResult)
        }
        return mcAccessToken to Instant.now().plusSeconds(expiresIn.toLong())
    }

    private suspend fun getMinecraftProfile(
        mcAccessToken: String
    ): Pair<String, String> {
        logger.info { "Getting Minecraft profile" }
        val profileResult = httpClient.get("https://api.minecraftservices.com/minecraft/profile") {
            bearerAuth(mcAccessToken)
            accept(ContentType.Application.Json)
        }.body<JsonObject>()
        val uuid = profileResult["id"]?.string
        val username = profileResult["name"]?.string
        if (uuid == null || username == null) invalidResponse(profileResult)
        return uuid to username
    }

    private suspend fun progress(emitter: suspend (AuthenticationState) -> Unit, progress: Int) {
        emitter(AuthenticationState.Progress(progress.toFloat() / 6))
    }

    private val JsonElement.string get() = jsonPrimitive.content
}
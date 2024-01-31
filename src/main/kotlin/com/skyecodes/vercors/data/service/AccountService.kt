package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.data.model.app.Account
import com.skyecodes.vercors.sha256
import com.skyecodes.vercors.ui.Localization
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
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import java.nio.file.Path
import java.time.Instant
import java.util.*
import kotlin.io.path.*

private val logger = KotlinLogging.logger { }

interface AccountService {
    val accountData: StateFlow<AccountData>
    val selectedAccount: Flow<Account>
    fun loadAccounts(): Job
    fun addAccount(account: Account)
    fun removeAccount(account: Account)
    fun startAuthentication(): Flow<AuthenticationState>
}

sealed interface AccountData {
    data object NotLoaded : AccountData

    @Serializable
    data class Loaded(val selected: String? = null, val accounts: List<Account> = emptyList()) : AccountData
}

class AccountServiceImpl(
    coroutineScope: CoroutineScope,
    storageService: StorageService,
    private val json: Json,
    private val httpClient: HttpClient,
    private val clientId: String,
    private val accountsFile: Path = storageService.configDir.resolve("accounts.json")
) : AccountService, CoroutineScope by coroutineScope {
    override val accountData = MutableStateFlow<AccountData>(AccountData.NotLoaded)
    override val selectedAccount: Flow<Account> = accountData
        .filterIsInstance<AccountData.Loaded>()
        .map { data -> data.accounts.firstOrNull { it.uuid == data.selected } }
        .filterNotNull()

    init {
        coroutineScope.launch { accountData.filterIsInstance<AccountData.Loaded>().drop(1).collect { save(it) } }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun loadAccounts() = launch(Dispatchers.IO) {
        logger.debug { "Loading accounts at location $accountsFile" }
        if (accountsFile.exists()) {
            try {
                accountData.value =
                    validate(accountsFile.inputStream().use { json.decodeFromStream<AccountData.Loaded>(it) })
            } catch (e: SerializationException) {
                val message = "Unable to load accounts"
                logger.error(e) { message }
                accountsFile.deleteExisting()
                init()
            }
        } else {
            init()
        }
        logger.info { "Accounts loaded" }
    }

    override fun addAccount(account: Account) {
        accountData.update {
            when (it) {
                is AccountData.Loaded -> validate(it.copy(selected = account.uuid, accounts = it.accounts + account))
                AccountData.NotLoaded -> throw RuntimeException() // impossible
            }
        }
    }

    override fun removeAccount(account: Account) {
        accountData.update {
            when (it) {
                is AccountData.Loaded -> validate(it.copy(accounts = it.accounts - account))
                AccountData.NotLoaded -> throw RuntimeException() // impossible
            }
        }
    }

    private suspend fun init() {
        val data = AccountData.Loaded()
        accountData.value = data
        save(data)
    }

    private fun validate(data: AccountData.Loaded): AccountData.Loaded {
        return if (data.accounts.isEmpty() && data.selected != null)
            data.copy(selected = null)
        else if (data.accounts.isNotEmpty() && (data.selected == null || data.selected !in data.accounts.map { it.uuid }))
            data.copy(selected = data.accounts.first().uuid)
        else
            data
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun save(accountData: AccountData.Loaded) = withContext(Dispatchers.IO) {
        accountsFile.createParentDirectories().outputStream().use { json.encodeToStream(accountData, it) }
        logger.info { "Accounts saved" }
    }

    override fun startAuthentication(): Flow<AuthenticationState> = channelFlow {
        withContext(Dispatchers.IO) {
            val result = runCatching {
                val codeVerifier = UUID.randomUUID().toString()
                val codeChallenge = codeVerifier.sha256().take(43)
                val (authorizationCode, port) = getAuthorizationCode(codeChallenge)
                val (msAccessToken, msRefreshToken) = getMicrosoftAccessToken(codeVerifier, authorizationCode, port)
                val (xblToken, userHash) = getXblToken(msAccessToken)
                val xstsToken = getXstsToken(xblToken, userHash)
                val (mcAccessToken, mcTokenExpiration) = getMinecraftAccessToken(userHash, xstsToken)
                val (uuid, username) = getMinecraftProfile(mcAccessToken, userHash, xstsToken)
                Account(username, uuid, msAccessToken, msRefreshToken, mcAccessToken, mcTokenExpiration)
            }
            result.onFailure {
                logger.error(it) { "An error has occured" }
                send(AuthenticationState.Error(it))
            }
            result.onSuccess {
                addAccount(it)
                send(AuthenticationState.Success(it))
            }
        }
    }

    private suspend fun ProducerScope<AuthenticationState>.getAuthorizationCode(
        codeChallenge: String
    ): Pair<String, Int> = coroutineScope {
        logger.info { "Getting authorization code" }
        phase(AuthenticationPhase.MicrosoftAuth)
        var authorizationCode: String? = null
        var error: String? = null
        val server = embeddedServer(CIO, 0) {
            routing {
                get("/") {
                    if (this.context.parameters.contains("code")) {
                        authorizationCode = this.context.parameters["code"]
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
            server.start()
            val port = server.resolvedConnectors()[0].port
            send(
                AuthenticationState.Waiting(
                    "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?" +
                            "client_id=$clientId" +
                            "&response_type=code" +
                            "&redirect_uri=http%3A%2F%2Flocalhost%3A$port" +
                            "&response_mode=query" +
                            "&scope=XboxLive.signin%20offline_access" +
                            "&code_challenge=$codeChallenge" +
                            "&code_challenge_method=S256"
                )
            )
            while (isActive && authorizationCode == null && error == null) delay(250)
            if (!isActive) throw AuthenticationException("Execution stopped")
            error?.let { throw AuthenticationException(it) }
            if (authorizationCode == null) throw AuthenticationException("Authentication failed")
            progress(1)
            authorizationCode!! to port
        }
        withContext(Dispatchers.IO) {
            server.stop()
            logger.debug { "Server stopped" }
        }
        result.getOrThrow()
    }

    private suspend fun ProducerScope<AuthenticationState>.getMicrosoftAccessToken(
        codeVerifier: String,
        authorizationCode: String,
        port: Int
    ): Pair<String, String> {
        logger.info { "Getting access token" }
        phase(AuthenticationPhase.MicrosoftToken)
        val tokenResult = httpClient.submitForm(
            url = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token",
            formParameters = parameters {
                set("client_id", clientId)
                set("scope", "XboxLive.signin")
                set("code", authorizationCode)
                set("redirect_uri", "http://localhost:$port")
                set("grant_type", "authorization_code")
                set("code_verifier", codeVerifier)
            }
        ).body<JsonObject>()
        val xblAccessToken = tokenResult["access_token"]?.string
        val refreshToken = tokenResult["refresh_token"]?.string
        if (xblAccessToken == null || refreshToken == null) {
            if (tokenResult.containsKey("error")) {
                val error = tokenResult["error"]!!.string
                val errorDescription = tokenResult["error_description"]!!.string
                throw AuthenticationException("$error: $errorDescription")
            } else {
                invalidResponse(tokenResult)
            }
        }
        progress(2)
        return xblAccessToken to refreshToken
    }

    private suspend fun ProducerScope<AuthenticationState>.getXblToken(
        xblAccessToken: String
    ): Pair<String, String> {
        logger.info { "Authenticating with Xbox Live" }
        phase(AuthenticationPhase.XboxLiveAuth)
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
        progress(3)
        return xblToken to userHash
    }

    private fun invalidResponse(result: JsonObject): Nothing {
        logger.error { "Invalid response: $result" }
        throw AuthenticationException("Invalid response, see logs for more info")
    }

    private suspend fun ProducerScope<AuthenticationState>.getXstsToken(
        xblToken: String,
        userHash: String
    ): String {
        logger.info { "Obtaining XSTS token" }
        phase(AuthenticationPhase.XstsToken)
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
        progress(4)
        return xstsToken
    }

    private suspend fun ProducerScope<AuthenticationState>.getMinecraftAccessToken(
        userHash: String,
        xstsToken: String
    ): Pair<String, Instant> {
        logger.info { "Authenticating with Minecraft" }
        phase(AuthenticationPhase.MinecraftAuth)
        val minecraftResult = httpClient.post("https://api.minecraftservices.com/authentication/login_with_xbox") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("identityToken", "XBL3.0 x=$userHash;$xstsToken")
            })
        }.body<JsonObject>()
        val mcAccessToken = minecraftResult["access_token"]?.string
        val expiresIn = minecraftResult["expired_in"]?.jsonPrimitive?.int
        if (mcAccessToken == null || expiresIn == null) {
            if (minecraftResult.containsKey("errorMessage")) throw AuthenticationException(minecraftResult["errorMessage"]?.string)
            else invalidResponse(minecraftResult)
        }
        progress(5)
        return mcAccessToken to Instant.now().plusSeconds(expiresIn.toLong())
    }

    private suspend fun ProducerScope<AuthenticationState>.getMinecraftProfile(
        mcAccessToken: String,
        userHash: String,
        xstsToken: String
    ): Pair<String, String> {
        logger.info { "Getting Minecraft profile" }
        phase(AuthenticationPhase.MinecraftProfile)
        val profileResult = httpClient.get("https://api.minecraftservices.com/minecraft/profile") {
            bearerAuth(mcAccessToken)
            accept(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("identityToken", "XBL3.0 x=$userHash;$xstsToken")
            })
        }.body<JsonObject>()
        val uuid = profileResult["id"]?.string
        val username = profileResult["name"]?.string
        if (uuid == null || username == null) invalidResponse(profileResult)
        progress(6)
        return uuid to username
    }

    private suspend fun ProducerScope<AuthenticationState>.phase(phase: AuthenticationPhase) {
        send(AuthenticationState.Phase(phase))
    }

    private suspend fun ProducerScope<AuthenticationState>.progress(progress: Int) {
        send(AuthenticationState.Progress(progress.toFloat() / AuthenticationPhase.entries.size))
    }

    private val JsonElement.string get() = jsonPrimitive.content
}

sealed interface AuthenticationState {
    data class Waiting(val url: String) : AuthenticationState
    data class Phase(val phase: AuthenticationPhase) : AuthenticationState
    data class Progress(val progress: Float) : AuthenticationState
    data class Error(val error: Throwable) : AuthenticationState
    data class Success(val account: Account) : AuthenticationState
}

enum class AuthenticationPhase(val localizedText: (Localization) -> String) {
    MicrosoftAuth(Localization::microsoftAuth),
    MicrosoftToken(Localization::microsoftToken),
    XboxLiveAuth(Localization::xboxLiveAuth),
    XstsToken(Localization::xstsToken),
    MinecraftAuth(Localization::minecraftAuth),
    MinecraftProfile(Localization::minecraftProfile),
}

class AuthenticationException(message: String? = "An error has occurred", cause: Throwable? = null) :
    Exception(message, cause)
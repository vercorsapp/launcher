package com.skyecodes.vercors.accounts

import androidx.compose.runtime.Immutable
import com.skyecodes.vercors.common.StorageService
import com.skyecodes.vercors.encodeBase64
import com.skyecodes.vercors.sha256
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
    val accountState: StateFlow<AccountState>
    val accountData: StateFlow<AccountState.Loaded?>
    val selectedAccount: StateFlow<Account?>
    fun loadAccounts(): Job
    fun addAccount(account: Account)
    fun removeAccount(account: Account)
    fun startAuthentication(): Flow<AuthenticationState>
    suspend fun validateToken(): Boolean
    fun selectAccount(account: Account)
}

sealed interface AccountState {
    data object NotLoaded : AccountState

    @Serializable
    @Immutable
    data class Loaded(val selected: String? = null, val accounts: List<Account> = emptyList()) : AccountState {
        val selectedAccount: Account? get() = accounts.find { it.uuid == selected }
    }
}

class AccountServiceImpl(
    coroutineScope: CoroutineScope,
    storageService: StorageService,
    private val json: Json,
    private val httpClient: HttpClient,
    private val clientId: String,
    private val accountsFile: Path = storageService.configDir.resolve("accounts.json")
) : AccountService, CoroutineScope by coroutineScope {
    override val accountState = MutableStateFlow<AccountState>(AccountState.NotLoaded)
    override val accountData: StateFlow<AccountState.Loaded?> = accountState
        .filterIsInstance<AccountState.Loaded>()
        .stateIn(this, SharingStarted.Eagerly, null)
    override val selectedAccount: StateFlow<Account?> = accountData
        .map { data -> data?.selectedAccount }
        .stateIn(this, SharingStarted.Eagerly, null)

    init {
        launch { accountData.filterNotNull().drop(1).collect { save(it) } }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun loadAccounts() = launch(Dispatchers.IO) {
        logger.debug { "Loading accounts at location $accountsFile" }
        if (accountsFile.exists()) {
            try {
                accountState.value =
                    validateAccountData(
                        accountsFile.inputStream().use { json.decodeFromStream<AccountState.Loaded>(it) })
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
        if (account.uuid in accountData.value!!.accounts.map { it.uuid }) {
            updateAccounts {
                it.copy(
                    selected = account.uuid,
                    accounts = it.accounts.map { oldAccount -> if (oldAccount.uuid == account.uuid) account else oldAccount })
            }
        } else {
            updateAccounts { it.copy(selected = account.uuid, accounts = it.accounts + account) }
        }
    }

    override fun removeAccount(account: Account) = updateAccounts { it.copy(accounts = it.accounts - account) }

    override fun selectAccount(account: Account) = updateAccounts { it.copy(selected = account.uuid) }

    private fun updateAccounts(updater: (AccountState.Loaded) -> AccountState.Loaded) {
        accountState.update {
            when (it) {
                is AccountState.Loaded -> validateAccountData(updater(it))
                else -> it
            }
        }
    }

    private suspend fun init() {
        val data = AccountState.Loaded()
        accountState.value = data
        save(data)
    }

    private fun validateAccountData(data: AccountState.Loaded): AccountState.Loaded {
        return if (data.accounts.isEmpty() && data.selected != null)
            data.copy(selected = null)
        else if (data.accounts.isNotEmpty() && (data.selected == null || data.selected !in data.accounts.map { it.uuid }))
            data.copy(selected = data.accounts.first().uuid)
        else
            data
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun save(accountState: AccountState.Loaded) = withContext(Dispatchers.IO) {
        accountsFile.createParentDirectories().outputStream().use { json.encodeToStream(accountState, it) }
        logger.info { "Accounts saved" }
    }

    override fun startAuthentication(): Flow<AuthenticationState> = channelFlow {
        withContext(Dispatchers.IO) {
            val result = runCatching {
                var progress = 0
                val state = UUID.randomUUID().toString()
                val codeVerifier = UUID.randomUUID().toString()
                val codeChallenge = codeVerifier.sha256().encodeBase64().take(43)
                val (authorizationCode, port) = getAuthorizationCode(state, codeChallenge)
                progress(++progress)
                val (msAccessToken, refreshToken) = getMicrosoftAccessToken(codeVerifier, authorizationCode, port)
                progress(++progress)
                val (xblToken, userHash) = getXblToken(msAccessToken)
                progress(++progress)
                val xstsToken = getXstsToken(xblToken, userHash)
                progress(++progress)
                val (token, exp) = getMinecraftAccessToken(userHash, xstsToken)
                progress(++progress)
                val (uuid, username) = getMinecraftProfile(token)
                progress(++progress)
                Account(username, uuid, refreshToken, token, exp)
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

    override suspend fun validateToken(): Boolean = withContext(Dispatchers.IO) {
        logger.info { "Validating token" }
        val selectedAccount = selectedAccount.value
        if (selectedAccount == null) {
            logger.info { "No account selected - requiring full login" }
            return@withContext false
        }
        if (selectedAccount.tokenData.exp > Instant.now().plusSeconds(60)) {
            return@withContext true
        }
        try {
            logger.info { "Verifying access token" }
            getMinecraftProfile(selectedAccount.tokenData.token)
            return@withContext true
        } catch (e: Throwable) {
            logger.info { "Access token expired - refreshing token" }
            try {
                val (msAccessToken, refreshToken) = refreshMicrosoftAccessToken(selectedAccount.tokenData.refreshToken)
                val (xblToken, userHash) = getXblToken(msAccessToken)
                val xstsToken = getXstsToken(xblToken, userHash)
                val (token, exp) = getMinecraftAccessToken(userHash, xstsToken)
                val (uuid, username) = getMinecraftProfile(token)
                addAccount(Account(username, uuid, refreshToken, token, exp))
                return@withContext true
            } catch (e: Throwable) {
                logger.info { "Unable to refresh token - requiring full login" }
                return@withContext false
            }
        }
    }

    private suspend fun ProducerScope<AuthenticationState>.getAuthorizationCode(
        state: String,
        codeChallenge: String
    ): Pair<String, Int> = coroutineScope {
        logger.info { "Getting authorization code" }
        var authorizationCode: String? = null
        var responseState: String? = null
        var error: String? = null
        val server = embeddedServer(CIO, 0) {
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
                            "&state=$state" +
                            "&prompt=select_account" +
                            "&code_challenge=$codeChallenge" +
                            "&code_challenge_method=S256"
                )
            )
            while (isActive && authorizationCode == null && error == null) delay(250)
            if (!isActive) throw AuthenticationException("Execution stopped")
            error?.let { throw AuthenticationException(it) }
            if (authorizationCode == null) throw AuthenticationException("Authentication failed")
            if (state != responseState) throw AuthenticationException("Response state doesn't match")
            authorizationCode!! to port
        }
        withContext(Dispatchers.IO) {
            server.stop()
            logger.debug { "Server stopped" }
        }
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
                val error = tokenResult["error"]!!.string
                val errorDescription = tokenResult["error_description"]!!.string
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

    private suspend fun ProducerScope<AuthenticationState>.progress(progress: Int) {
        send(AuthenticationState.Progress(progress.toFloat() / 6))
    }

    private val JsonElement.string get() = jsonPrimitive.content
}

sealed interface AuthenticationState {
    data class Waiting(val url: String) : AuthenticationState
    data class Progress(val progress: Float) : AuthenticationState
    data class Error(val error: Throwable) : AuthenticationState
    data class Success(val account: Account) : AuthenticationState
    data object Closed : AuthenticationState
}

class AuthenticationException(message: String? = "An error has occurred", cause: Throwable? = null) :
    Exception(message, cause)
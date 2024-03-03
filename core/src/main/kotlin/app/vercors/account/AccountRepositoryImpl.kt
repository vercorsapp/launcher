package app.vercors.account

import app.vercors.system.storage.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.*

private val logger = KotlinLogging.logger { }

class AccountRepositoryImpl(
    private val storageService: StorageService,
    private val json: Json,
    private val accountsFileName: String = "accounts.json"
) : AccountRepository {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun loadAccounts(context: CoroutineContext): AccountListData = withContext(context) {
        val accountsFile = storageService.basePath.resolve(accountsFileName)
        try {
            logger.debug { "Loading accounts at location $accountsFile" }
            val data = if (accountsFile.exists()) {
                try {
                    accountsFile.inputStream().use { json.decodeFromStream<AccountListData>(it) }
                } catch (e: SerializationException) {
                    val message = "Unable to load accounts - resetting account storage"
                    logger.error(e) { message }
                    accountsFile.deleteExisting()
                    init()
                }
            } else {
                init()
            }
            logger.debug { "Loaded accounts" }
            data
        } catch (e: Exception) {
            throw AccountRepositoryException("Unable to load accounts", e)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun saveAccounts(accounts: AccountListData, context: CoroutineContext) {
        withContext(context) {
            val accountsFile = storageService.basePath.resolve(accountsFileName)
            accountsFile.createParentDirectories().outputStream().use { json.encodeToStream(accounts, it) }
            logger.info { "Accounts saved" }
        }
    }

    private suspend fun init() = AccountListData().also { saveAccounts(it) }
}
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

package app.vercors.account

import app.vercors.system.storage.StorageService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.withContext
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

    override suspend fun saveAccounts(accounts: AccountListData, context: CoroutineContext) {
        withContext(context) {
            val accountsFile = storageService.basePath.resolve(accountsFileName)
            accountsFile.createParentDirectories().outputStream().use { json.encodeToStream(accounts, it) }
            logger.info { "Accounts saved" }
        }
    }

    private suspend fun init() = AccountListData().also { saveAccounts(it) }
}
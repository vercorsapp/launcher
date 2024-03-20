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

import app.vercors.common.Resource
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository that manages the accounts registered in the application
 */
interface AccountRepository {
    /**
     * Contains the loading state of the account list (loaded or not loaded).
     */
    val loadingState: StateFlow<Resource<AccountList>>

    /**
     * Contains the state of the account list (its value if loaded, or null if not loaded).
     */
    val state: StateFlow<AccountList?>

    /**
     * Contains the current account list.
     * @throws NullPointerException if the account list is not loaded
     */
    val current: AccountList

    /**
     * Contains the state of the currently selected account, or null if there is no selected account.
     */
    val selectedState: StateFlow<Account?>

    /**
     * Contains the currently selected account.
     * @throws NullPointerException if the account list is not loaded
     */
    val currentSelected: Account

    /**
     * Loads the account list and automatically updates the state when done.
     */
    suspend fun loadAccounts()

    /**
     * Adds an account the account list, selects it, and automatically saves when done.
     * If an account with the same UUID already exists, updates the existing one and selects it.
     * @param account The account to add
     */
    suspend fun addAccount(account: Account)

    /**
     * Removes an account from account list, unselects it if it was selected, and automatically saves when done.
     * @param uuid The uuid of the account to remove
     */
    suspend fun removeAccount(uuid: String)

    /**
     * Selects an account from the account list and automatically saves when done.
     * @param uuid The uuid of the account to select
     */
    suspend fun selectAccount(uuid: String)

    /**
     * Updates the account list and automatically saves when done.
     * @param updater The account list updater
     */
    suspend fun updateAccounts(updater: (AccountList) -> AccountList)
}
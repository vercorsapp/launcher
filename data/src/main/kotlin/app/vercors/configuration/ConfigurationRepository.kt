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

package app.vercors.configuration

import app.vercors.common.Resource
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository that manages the configuration of the application
 */
interface ConfigurationRepository {
    /**
     * Contains the loading state of the configuration (loaded or not loaded).
     */
    val loadingState: StateFlow<Resource<Configuration>>

    /**
     * Contains the state of the configuration (its value if loaded, or null if not loaded).
     */
    val state: StateFlow<Configuration?>

    /**
     * Contains the current configuration.
     * @throws NullPointerException if the configuration is not loaded
     */
    val current: Configuration

    /**
     * Loads the configuration and automatically updates the state when done.
     */
    suspend fun loadConfiguration()

    /**
     * Updates the configuration and automatically saves when done.
     * @param updater The configuration updater
     */
    suspend fun updateConfiguration(updater: (Configuration) -> Configuration)

    /**
     * Initializes the configuration and automatically saves when done.
     * @param configuration The configuration to initialize
     */
    suspend fun initializeConfiguration(configuration: Configuration)
}